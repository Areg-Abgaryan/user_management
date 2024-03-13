/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.UserGroupEntity;

import java.util.Collection;

public interface IUserGroupService {

    Collection<UserGroupEntity> getAllUserGroups();
}
