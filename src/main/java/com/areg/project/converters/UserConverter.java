/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.UserInputDTO;
import com.areg.project.models.dtos.UserOutputDTO;
import com.areg.project.models.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public Set<UserInputDTO> fromEntityToDto(Set<UserEntity> userEntities) {

        if (userEntities == null || userEntities.isEmpty()) {
            return Collections.emptySet();
        }

        return userEntities.stream().map(this::fromEntityToDto).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public UserInputDTO fromEntityToDto(UserEntity userEntity) {

        if (userEntity == null) {
            return null;
        }

        final var userDTO = new UserInputDTO();
        userDTO.setId(userEntity.getExternalId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        return userDTO;
    }

    public UserOutputDTO fromEntityToOutputDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        final var userDTO = new UserOutputDTO();
        userDTO.setId(userEntity.getExternalId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        return userDTO;
    }

    public UserEntity fromInputDtoToEntity(UserInputDTO userInputDto) {
        if (userInputDto == null) {
            return null;
        }

        final var userEntity = new UserEntity();
        userEntity.setEmail(userInputDto.getEmail());
        userEntity.setFirstName(userInputDto.getFirstName());
        userEntity.setLastName(userInputDto.getLastName());
        return userEntity;
    }
}
