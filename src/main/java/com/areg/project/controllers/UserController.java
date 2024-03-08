/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.managers.UserManager;
import com.areg.project.models.dtos.responses.user.UserLoginResponse;
import com.areg.project.models.dtos.responses.user.UserSignupResponse;
import com.areg.project.models.dtos.requests.user.UserLoginDTO;
import com.areg.project.models.dtos.requests.user.UserSignUpDTO;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
import com.areg.project.security.jwt.JwtProvider;
import com.areg.project.security.jwt.JwtToken;
import com.areg.project.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InvalidObjectException;
import java.util.Set;

import static com.areg.project.controllers.EndpointsConstants.SIGNUP;
import static com.areg.project.controllers.EndpointsConstants.USER;
import static com.areg.project.controllers.EndpointsConstants.VERIFY_EMAIL;

@RestController("user-controller")
@RequestMapping(USER)
@Tag(name = "User Controller")
public class UserController {

    private final UserManager userManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserManager userManager, JwtProvider jwtProvider) {
        this.userManager = userManager;
        this.jwtProvider = jwtProvider;
    }


    //  Register the user in the system with status UNVERIFIED, after the api below change as ACTIVE
    @Operation(summary = "User Sign up", description = "Registration of a new user in the system")
    @PostMapping(SIGNUP)
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDTO userSignUpDto) {
        //  FIXME !! Validate email, password, firstName, lastName
        //  FIXME !! Add validation, return message for unverified users
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

    @Operation(summary = "User Login", description = "Login user in the system. The user with the JWT token is returned")
    @PostMapping(EndpointsConstants.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDto) {

        //  FIXME !! Move to UserManager class
        final String email = loginDto.getEmail();
        final String password = loginDto.getPassword();

        try {
            if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
                throw new InvalidObjectException("Login arguments cannot be blank");
            }

            final var token = new UsernamePasswordToken(email, password);
            final UserSignupResponse userResponse = userManager.findUserByEmail(email);

            //  Login and update last login time
            SecurityUtils.getSubject().login(token);
            userManager.updateLastLoginTime(email, Utils.getCurrentDateAndTime());

            // Generate JWT token with user permissions
            final Set<String> setOfPermissions = userManager.createUserPermissionsWildcards(userResponse.getId());
            final String jwtTokenString = jwtProvider.createJwtToken(email, setOfPermissions);
            final JwtToken jwtToken = JwtToken.create(jwtTokenString);
            final var loginOutputDto = new UserLoginResponse(userResponse.getFirstName(), userResponse.getLastName(), jwtToken);

            return ResponseEntity.ok(loginOutputDto);
        }  catch (UsernameNotFoundException ue) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with that email was not found");
        } catch (AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong email provided");
        } catch (org.apache.shiro.authc.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong password provided");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }
}