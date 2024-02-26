/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.UserConverter;
import com.areg.project.models.dtos.UserSignUpDTO;
import com.areg.project.models.responses.UserSignUpResponse;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.util.UUID;

@Service
public class UserManager {

    private final UserService userService;
    private final UserConverter userConverter;
    private final EncryptionManager encryptionManager;

    @Autowired
    public UserManager(UserService userService, UserConverter userConverter, EncryptionManager encryptionManager) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.encryptionManager = encryptionManager;
    }

    public UserSignUpResponse signUp(UserSignUpDTO userSignUpDto) throws InvalidObjectException {

        if (! isValidUserDTO(userSignUpDto)) {
            //logger.error(_msg(sessionId, email, "Invalid data provided for the user : " + email));
            throw new InvalidObjectException("Data cannot be empty");
        }

        final UserEntity userEntity = userConverter.fromSignUpInputToEntity(userSignUpDto);
        final String salt = encryptionManager.generateSalt();
        userEntity.setSalt(salt);

        final String encryptedPassword = encryptionManager.encrypt(userSignUpDto.getPassword(), salt);
        userEntity.setPassword(encryptedPassword);

        final UserEntity savedUserEntity = userService.createUser(userEntity);
        final UserSignUpResponse savedUserDto = userConverter.fromEntityToSignUpResponse(savedUserEntity);

        //logger.info(_msg(Utils.getSessionId(), savedUserDto.getId().toString(), "User was successfully added"));
        return savedUserDto;
    }

    public UserSignUpResponse findUserById(UUID id) {
        final UserEntity userEntity = userService.findUserById(id);
        return userConverter.fromEntityToSignUpResponse(userEntity);
    }


    private boolean isValidUserDTO(UserSignUpDTO userSignUpDto) {
        return StringUtils.isNotBlank(userSignUpDto.getEmail()) && StringUtils.isNotBlank(userSignUpDto.getPassword())
                && StringUtils.isNotBlank(userSignUpDto.getFirstName()) && StringUtils.isNotBlank(userSignUpDto.getLastName());
    }
}