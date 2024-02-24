/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.logging.UserSessionLogger;
import com.areg.project.managers.UserManager;
import com.areg.project.models.dtos.UserInputDTO;
import com.areg.project.models.dtos.UserOutputDTO;
import com.areg.project.utils.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InvalidObjectException;
import java.util.UUID;

import static com.areg.project.logging.UserSessionLogger._msg;

@RestController("user-controller")
@RequestMapping(EndpointsConstants.API)
@Api(value = "User Controller", tags = { "User Controller" })
public class UserController {

    private static String sessionId, email;
    private static Logger logger;

    private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }


    @ApiOperation(value = "Get user", notes = "The user with the selected id is returned")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserOutputDTO> getUser(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userManager.findUserById(id));
    }

    @ApiOperation(value = "User Sign up", notes = "Registration of a new user in the system")
    @PostMapping("/user/signup")
    public ResponseEntity<?> signUp(@RequestBody UserInputDTO userInputDTO) {

        initUserSessionLogger(userInputDTO);

        try {
            if (! isValidUserDTO(userInputDTO)) {
                logger.error(_msg(sessionId, email, "Invalid data provided for the user : " + email));
                throw new InvalidObjectException("Data cannot be empty");
            }
            return ResponseEntity.ok(userManager.signUp(userInputDTO, logger));
        } catch (InvalidObjectException e) {
            logger.error(_msg(sessionId, email, "Invalid data provided"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided");
        } catch (DataIntegrityViolationException e) {
            logger.error(_msg(sessionId, email, "User with email " + email + "already exists in the system"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with such email already exists");
        }
    }

    private static void initUserSessionLogger(UserInputDTO userInputDTO) {
        sessionId = Utils.getSessionId();
        email = userInputDTO.getEmail();
        logger = UserSessionLogger.getLogger(email, sessionId);
    }

    private boolean isValidUserDTO(UserInputDTO userInputDTO) {
        return StringUtils.isNotBlank(userInputDTO.getEmail()) && StringUtils.isNotBlank(userInputDTO.getPassword())
                && StringUtils.isNotBlank(userInputDTO.getFirstName()) && StringUtils.isNotBlank(userInputDTO.getLastName());
    }
}