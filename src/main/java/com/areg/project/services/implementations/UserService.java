/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.UserStatus;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.repositories.IUserRepository;
import com.areg.project.services.interfaces.IUserService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;

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
    public UserEntity createUser(UserEntity userEntity) {
        userEntity.setExternalId(UUID.randomUUID());
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCreated(Utils.getCurrentDateAndTime());
        userEntity.setUpdated(Utils.getCurrentDateAndTime());
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity findUserById(UUID id) throws NoResultException {
        return userRepository.findUserEntityByExternalId(id)
                .orElseThrow(
                        () -> new NoResultException(id.toString())
                );
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(
                        () -> new NoResultException("User with email '" + email + "' not found")
                );
    }


    public void updateLastLoginTime(String username, LocalDateTime lastLoginDate) {
        final Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(username);
        if (userEntity.isEmpty()) {
            return;
        }
        userEntity.get().setUpdated(lastLoginDate);
        userRepository.save(userEntity.get());
    }
}