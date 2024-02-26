/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.managers.UserManager;
import com.areg.project.models.dtos.UserSignUpDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InvalidObjectException;

@RestController("user-controller")
@RequestMapping(EndpointsConstants.USER)
@Tag(name = "User Controller")
public class UserController {

    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }


    @Operation(summary = "User Sign up", description = "Registration of a new user in the system")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDTO userSignUpDto) {

        //logger.info(_msg(Utils.getSessionId(), email, "Request for registering a new user : " + email));

        try {
            return ResponseEntity.ok(userManager.signUp(userSignUpDto)); //, logger));
        } catch (InvalidObjectException e) {
            //logger.error(_msg(sessionId, email, "Invalid data provided"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        } catch (DataIntegrityViolationException e) {
            //logger.error(_msg(sessionId, email, "User with email " + email + "already exists in the system"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with such email already exists");
        }
    }
}