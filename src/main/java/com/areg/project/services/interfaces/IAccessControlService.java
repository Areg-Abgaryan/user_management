/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.AccessControlEntity;
import com.areg.project.models.entities.UserGroupEntity;

public interface IAccessControlService {

    AccessControlEntity getByUserGroupId(UserGroupEntity userGroup);
}
