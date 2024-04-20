/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.AccessControlEntity;

import java.util.Set;

public interface IAccessControlService {

    Set<AccessControlEntity> getByUserGroupIds(Set<Long> userGroupIds);
}
