/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IUserService {

    UserEntity createUnverifiedUser(UserEntity userEntity);

    UserEntity saveVerifiedUser(UserEntity userEntity);

    void updateWithNoOtpData(UserEntity userEntity);

    UserEntity findUserById(UUID id);

    UserEntity findUserByEmail(String email);

    void updateLastLoginTime(String email, LocalDateTime lastLoginDate);
}
