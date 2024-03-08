/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.requests.UserSignUpDTO;
import com.areg.project.models.dtos.responses.UserSignupResponse;
import com.areg.project.models.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserSignupResponse fromEntityToSignUpResponse(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        final var signupResponse = new UserSignupResponse();
        signupResponse.setId(userEntity.getExternalId());
        signupResponse.setEmail(userEntity.getEmail());
        signupResponse.setFirstName(userEntity.getFirstName());
        signupResponse.setLastName(userEntity.getLastName());
        return signupResponse;
    }

    public UserEntity fromSignUpDtoToEntity(UserSignUpDTO signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        final var userEntity = new UserEntity();
        userEntity.setEmail(signUpDto.getEmail());
        userEntity.setFirstName(signUpDto.getFirstName());
        userEntity.setLastName(signUpDto.getLastName());
        return userEntity;
    }
}