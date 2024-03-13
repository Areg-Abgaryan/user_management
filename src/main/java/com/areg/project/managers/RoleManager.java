/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.RoleConverter;
import com.areg.project.models.dtos.RoleDTO;
import com.areg.project.services.implementations.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleManager {

    private final RoleService roleService;
    private final RoleConverter roleConverter;

    @Autowired
    public RoleManager(RoleService roleService, RoleConverter roleConverter) {
        this.roleService = roleService;
        this.roleConverter = roleConverter;
    }


    public Set<RoleDTO> getAllRoles() {
        return roleConverter.fromEntityToDto(roleService.getAllRoles());
    }
}
