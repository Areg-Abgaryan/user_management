/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.UserSignUpDTO;
import com.areg.project.models.responses.UserSignUpResponse;
import com.areg.project.models.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserSignUpResponse fromEntityToSignUpResponse(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        final var userDto = new UserSignUpResponse();
        userDto.setId(userEntity.getExternalId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        return userDto;
    }

    public UserEntity fromSignUpInputToEntity(UserSignUpDTO userSignUpDto) {
        if (userSignUpDto == null) {
            return null;
        }

        final var userEntity = new UserEntity();
        userEntity.setEmail(userSignUpDto.getEmail());
        userEntity.setFirstName(userSignUpDto.getFirstName());
        userEntity.setLastName(userSignUpDto.getLastName());
        return userEntity;
    }
}