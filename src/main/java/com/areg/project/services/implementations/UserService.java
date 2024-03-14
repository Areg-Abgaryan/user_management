/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.exceptions.UserNotFoundException;
import com.areg.project.models.UserStatus;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.repositories.IUserRepository;
import com.areg.project.services.interfaces.IUserService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
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
        entity.setExternalId(UUID.randomUUID());
        entity.setCreated(Utils.getCurrentDateAndTime());
        entity.setUpdated(Utils.getCurrentDateAndTime());
        return userRepository.save(entity);
    }

    @Override
    @Transactional
    public UserEntity saveVerifiedUser(UserEntity entity) {
        entity.setStatus(UserStatus.ACTIVE);
        return userRepository.saveAndFlush(entity);
    }

    @Override
    public void removeOtpData(UserEntity entity) {
        entity.setOtp(0);
        entity.setOtpCreationTime(0);
        userRepository.save(entity);
    }

    @Override
    public UserEntity findUserById(UUID id) throws UserNotFoundException {
        return userRepository.findByExternalId(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserEntity findUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public void updateLastLoginTime(String email, LocalDateTime lastLoginDate) {
        final Optional<UserEntity> entity = userRepository.findByEmail(email);
        if (entity.isEmpty()) {
            return;
        }
        entity.get().setLastLoginTime(lastLoginDate);
        userRepository.saveAndFlush(entity.get());
    }

    @Override
    public List<UserEntity> getAllActiveUsers() {
        return userRepository.getAllActiveUsers();
    }
}