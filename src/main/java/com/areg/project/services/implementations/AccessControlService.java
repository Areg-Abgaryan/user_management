/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.exceptions.AccessControlNotFoundException;
import com.areg.project.models.entities.AccessControlEntity;
import com.areg.project.repositories.IAccessControlRepository;
import com.areg.project.services.interfaces.IAccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccessControlService implements IAccessControlService {

    private final IAccessControlRepository accessControlRepository;

    @Autowired
    public AccessControlService(IAccessControlRepository accessControlRepository) {
        this.accessControlRepository = accessControlRepository;
    }

    @Override
    public Set<AccessControlEntity> getByUserGroupIds(Set<Long> userGroupIds) {
        final Set<AccessControlEntity> accessControls = accessControlRepository.findByUserGroupIdIn(userGroupIds);
        if (accessControls.isEmpty()) {
            throw new AccessControlNotFoundException(userGroupIds);
        }

        return accessControls;
    }
}
