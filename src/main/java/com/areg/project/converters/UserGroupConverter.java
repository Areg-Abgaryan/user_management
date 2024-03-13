/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.UserGroupDTO;
import com.areg.project.models.entities.UserGroupEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserGroupConverter {

    public Set<UserGroupDTO> fromEntityToDto(Collection<UserGroupEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(this::fromEntityToDto).collect(Collectors.toCollection(HashSet::new));
    }


    private UserGroupDTO fromEntityToDto(UserGroupEntity entity) {
        if (entity == null) {
            return null;
        }

        final var userGroupDto = new UserGroupDTO();
        userGroupDto.setId(entity.getExternalId());
        userGroupDto.setName(entity.getName());
        return userGroupDto;
    }
}
