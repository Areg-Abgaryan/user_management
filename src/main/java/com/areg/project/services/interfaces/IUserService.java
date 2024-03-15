/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IUserService {

    UserEntity createUnverifiedUser(UserEntity userEntity);

    UserEntity saveVerifiedUser(UserEntity userEntity);

    void removeOtpData(UserEntity userEntity);

    void updateLastLoginTime(String email, LocalDateTime lastLoginDate);

    void updateUser(UserEntity entity);


    UserEntity getActiveUserByEmail(String email);

    UserEntity getUserByEmail(String email);

    List<UserEntity> getAllActiveUsers();
}
