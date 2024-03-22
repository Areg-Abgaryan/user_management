/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.enums.UserStatus;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.repositories.IUserRepository;
import com.areg.project.services.interfaces.IUserService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserEntity createUnverifiedUser(UserEntity entity) {
        entity.setStatus(UserStatus.UNVERIFIED);
        entity.setUuid(UUID.randomUUID());
        entity.setCreationDate(Utils.getCurrentDateAndTime());
        entity.setUpdateDate(Utils.getCurrentDateAndTime());
        return userRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public UserEntity saveVerifiedUser(UserEntity entity) {
        entity.setStatus(UserStatus.ACTIVE);
        return userRepository.saveAndFlush(entity);
    }

    @Override
    public void updateUser(UserEntity entity) {
        userRepository.saveAndFlush(entity);
    }

    @Override
    public void updateLastLoginTime(String email, LocalDateTime lastLoginDate) {
        final Optional<UserEntity> entity = userRepository.findActiveUserByEmail(email);
        if (entity.isEmpty()) {
            return;
        }
        entity.get().setLastLoginTime(lastLoginDate);
        userRepository.saveAndFlush(entity.get());
    }

    @Override
    public UserEntity getActiveUserByEmail(String email) {
        return userRepository.findActiveUserByEmail(email).orElse(null);
    }

    @Override
    public UserEntity getActiveUserByUuid(UUID uuid) {
        return userRepository.findActiveUserByUuid(uuid).orElse(null);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<UserEntity> getAllActiveUsers() {
        return userRepository.getAllActiveUsers();
    }

    @Override
    public void removeOtpData(UserEntity entity) {
        entity.setOtp(0);
        entity.setOtpCreationTime(0);
        userRepository.saveAndFlush(entity);
    }
}