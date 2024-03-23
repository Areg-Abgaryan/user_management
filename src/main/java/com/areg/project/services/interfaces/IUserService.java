/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IUserService {

    UserEntity createUnverifiedUser(UserEntity userEntity);

    UserEntity saveVerifiedUser(UserEntity userEntity);

    void removeOtpData(UserEntity userEntity);

    void updateLastLoginDate(String email, long lastLoginDate);

    void updateUser(UserEntity entity);


    UserEntity getActiveUserByEmail(String email);

    UserEntity getActiveUserByUuid(UUID uuid);

    UserEntity getUserByEmail(String email);

    List<UserEntity> getAllActiveUsers();
}
