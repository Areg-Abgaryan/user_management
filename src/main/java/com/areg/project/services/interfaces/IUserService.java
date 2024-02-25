/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.UserEntity;

import java.util.UUID;

public interface IUserService {

    UserEntity findUserById(UUID id);

    UserEntity findUserByEmail(String email);
}