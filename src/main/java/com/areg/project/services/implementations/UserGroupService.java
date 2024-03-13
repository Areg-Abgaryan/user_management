/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.entities.UserGroupEntity;
import com.areg.project.repositories.IUserGroupRepository;
import com.areg.project.services.interfaces.IUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserGroupService implements IUserGroupService {

    private final IUserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupService(IUserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }


    @Override
    public Collection<UserGroupEntity> getAllUserGroups() {
        return userGroupRepository.findAll();
    }
}
