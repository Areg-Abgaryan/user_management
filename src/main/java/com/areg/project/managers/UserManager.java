/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.UserConverter;
import com.areg.project.models.dtos.UserDTO;
import com.areg.project.models.dtos.responses.user.UserSignupResponse;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserManager {

    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserManager(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }


    public Set<UserDTO> getAllActiveUsers() {
        return userConverter.fromEntityToDto(userService.getAllActiveUsers());
    }

    public void updateLastLoginDate(String email) {
        userService.updateLastLoginDate(email, Utils.getEpochSecondsNow());
    }

    public UserSignupResponse getActiveUserByEmail(String email) {
        final UserEntity entity = userService.getActiveUserByEmail(email);
        return userConverter.fromEntityToSignUpResponse(entity);
    }
}