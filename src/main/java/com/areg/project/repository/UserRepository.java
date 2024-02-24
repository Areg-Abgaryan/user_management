/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repository;

import com.areg.project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findUserEntityByExternalId(UUID userId);
}

