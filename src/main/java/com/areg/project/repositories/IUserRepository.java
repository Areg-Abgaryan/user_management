/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repositories;

import com.areg.project.models.UserStatus;
import com.areg.project.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByExternalId(UUID userId);

    Optional<UserEntity> findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE \"user_status\" = 'ACTIVE'")
    List<UserEntity> getAllActiveUsers();

    void deleteAllByStatus(UserStatus status);
}