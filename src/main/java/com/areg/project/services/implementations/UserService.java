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

import java.time.LocalDateTime;
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
    public UserEntity createUnverifiedUser(UserEntity userEntity) {
        userEntity.setStatus(UserStatus.UNVERIFIED);
        userEntity.setExternalId(UUID.randomUUID());
        userEntity.setCreated(Utils.getCurrentDateAndTime());
        userEntity.setUpdated(Utils.getCurrentDateAndTime());
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity saveVerifiedUser(UserEntity userEntity) {
        userEntity.setStatus(UserStatus.ACTIVE);
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void updateWithNoOtpData(UserEntity userEntity) {
        userEntity.setOtp(0);
        userEntity.setOtpCreationTime(0);
        userRepository.save(userEntity);
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
        final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty()) {
            return;
        }
        userEntity.get().setUpdated(lastLoginDate);
        userRepository.saveAndFlush(userEntity.get());
    }
}