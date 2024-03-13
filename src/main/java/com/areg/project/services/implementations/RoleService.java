/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.entities.RoleEntity;
import com.areg.project.repositories.IRoleRepository;
import com.areg.project.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Autowired
    public Collection<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }
}
