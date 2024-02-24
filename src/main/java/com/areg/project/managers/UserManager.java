/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.UserConverter;
import com.areg.project.models.dtos.UserInputDTO;
import com.areg.project.models.dtos.UserOutputDTO;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.areg.project.logging.UserSessionLogger._msg;

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

    public UserOutputDTO signUp(UserInputDTO userInputDto, Logger logger) {

        final String email = userInputDto.getEmail();
        logger.info(_msg(Utils.getSessionId(), email, "Request for registering a new user : " + email));

        final UserEntity userEntity = userConverter.fromInputDtoToEntity(userInputDto);
        final String salt = encryptionManager.generateSalt();
        userEntity.setSalt(salt);

        final String encryptedPassword = encryptionManager.encrypt(userInputDto.getPassword(), salt);
        userEntity.setPassword(encryptedPassword);

        final UserOutputDTO savedUserDto = userConverter.fromEntityToOutputDto(userService.createUser(userEntity));

        logger.info(_msg(Utils.getSessionId(), savedUserDto.getId().toString(), "User was successfully added"));
        return savedUserDto;
    }

    public UserOutputDTO findUserById(UUID id) {
        final UserEntity userEntity = userService.findUserById(id);
        return userConverter.fromEntityToOutputDto(userEntity);
    }
}
