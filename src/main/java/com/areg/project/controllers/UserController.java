/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.managers.UserManager;
import com.areg.project.models.dtos.UserSignUpDTO;
import com.areg.project.models.dtos.UserVerifyEmailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InvalidObjectException;

import static com.areg.project.controllers.EndpointsConstants.SIGNUP;
import static com.areg.project.controllers.EndpointsConstants.USER;
import static com.areg.project.controllers.EndpointsConstants.VERIFY_EMAIL;

@RestController("user-controller")
@RequestMapping(USER)
@Tag(name = "User Controller")
public class UserController {

    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }


    //  Register the user in the system with status UNVERIFIED, after the api below change as ACTIVE
    @Operation(summary = "User Sign up", description = "Registration of a new user in the system")
    @PostMapping(SIGNUP)
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDTO userSignUpDto) {
        //  FIXME !! Validate email, password, firstName, lastName
        //  FIXME !! Run a background job for removing all UNVERIFIED users after some time
        try {
            return ResponseEntity.ok(userManager.createUnverifiedUser(userSignUpDto));
        } catch (InvalidObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with such email already exists");
        }
    }

    //  FIXME !! Add exception handling for invalid, wrong email
    @Operation(summary = "User Email Verification", description = "Verification of email during user sign up using OTP")
    @PostMapping(SIGNUP + VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestBody UserVerifyEmailDTO verifyEmailDto) {
        try {
            return ResponseEntity.ok(userManager.verifyUserEmail(verifyEmailDto));
        } catch (org.apache.shiro.authc.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong password provided");
        } catch (InvalidObjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }
}