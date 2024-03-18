/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.exceptions.BlankInputDataException;
import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.OtpTimeoutException;
import com.areg.project.exceptions.UserNotFoundException;
import com.areg.project.exceptions.WrongOtpException;
import com.areg.project.managers.AuthManager;
import com.areg.project.models.dtos.requests.user.UserLoginDTO;
import com.areg.project.models.dtos.requests.user.UserSignUpDTO;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.internet.AddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.areg.project.controllers.EndpointsConstants.AUTH;
import static com.areg.project.controllers.EndpointsConstants.SIGNUP;
import static com.areg.project.controllers.EndpointsConstants.VERIFY_EMAIL;

@RestController("auth-controller")
@RequestMapping(AUTH)
@Tag(name = "Auth Controller")
public class AuthController {

    private final AuthManager authManager;

    @Autowired
    public AuthController(AuthManager authManager) {
        this.authManager = authManager;
    }


    @Operation(summary = "User Sign up", description = "Registration of a new user in the system with 'UNVERIFIED' status")
    @PostMapping(SIGNUP)
    public ResponseEntity<?> signup(@RequestBody UserSignUpDTO userSignUpDto) {
        try {
            return ResponseEntity.ok(authManager.createUnverifiedUser(userSignUpDto));
        } catch (ForbiddenOperationException foe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(foe.getMessage());
        } catch (BlankInputDataException | IllegalArgumentException | AddressException ee) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ee.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with such email already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Operation(summary = "User Email Verification", description = "Making user's status 'ACTIVE' by verifying the email using OTP")
    @PostMapping(SIGNUP + VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestBody UserVerifyEmailDTO verifyEmailDto) {
        try {
            return ResponseEntity.ok(authManager.verifyUserEmail(verifyEmailDto));
        }  catch (ForbiddenOperationException foe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(foe.getMessage());
        } catch (UserNotFoundException une) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(une.getMessage());
        } catch (WrongOtpException | OtpTimeoutException pe) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(pe.getMessage());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (org.apache.shiro.authc.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password provided");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Operation(summary = "User Log in", description = "Log in the user in the system. The user with the JWT token is returned")
    @PostMapping(EndpointsConstants.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDto) {
        try {
            return ResponseEntity.ok(authManager.login(loginDto));
        } catch (BlankInputDataException | IllegalArgumentException ide) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ide.getMessage());
        } catch (UserNotFoundException ue) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't find a user with this email");
        } catch (org.apache.shiro.authc.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong password provided");
        } catch (ForbiddenOperationException foe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(foe.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping(EndpointsConstants.LOGOUT)
    @Operation(summary = "User log out", description = "Log out the user from the system")
    public ResponseEntity<?> logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            authManager.logout(jwtToken);
        } catch (ExpiredJwtException eje) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expired jwt token provided");
        } catch (MalformedJwtException | SignatureException je) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid jwt provided");
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
        return ResponseEntity.ok().build();
    }
}