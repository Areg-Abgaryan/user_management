/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.RoleEntity;

import java.util.Collection;

public interface IRoleService {

    Collection<RoleEntity> getAllRoles();
}
