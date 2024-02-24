/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.UserStatus;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.repositories.UserRepository;
import com.areg.project.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserEntity findUserById(UUID id) throws NoResultException {
        return userRepository.findUserEntityByExternalId(id)
                .orElseThrow(
                        () -> new NoResultException(id.toString())
                );
    }

    public UserEntity findUserByEmail(String email) throws NoResultException {
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
        userEntity.get().setLastLoginTime(lastLoginDate);
        userRepository.save(userEntity.get());
    }


    public UserEntity createUser(UserEntity userEntity) {
        userEntity.setExternalId(UUID.randomUUID());
        userEntity.setStatus(UserStatus.ACTIVE);
        return userRepository.save(userEntity);
    }
}