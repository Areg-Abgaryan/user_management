/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.RoleDTO;
import com.areg.project.models.entities.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleConverter {

    public Set<RoleDTO> fromEntityToDto(Collection<RoleEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(this::fromEntityToDto).collect(Collectors.toCollection(HashSet::new));
    }


    private RoleDTO fromEntityToDto(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        final var roleDto = new RoleDTO();
        roleDto.setName(entity.getName());
        return roleDto;
    }
}
