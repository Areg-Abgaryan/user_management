/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repositories;

import com.areg.project.models.enums.UserStatus;
import com.areg.project.models.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    void deleteAllByStatus(UserStatus status);

    Optional<UserEntity> findByEmail(String email);

    @EntityGraph(attributePaths = "userGroups")
    @Query("SELECT u FROM user u WHERE u.email = :email AND u.status = 'ACTIVE'")
    Optional<UserEntity> findActiveUserByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM \"user\" WHERE uuid = :uuid AND status = 'ACTIVE'")
    Optional<UserEntity> findActiveUserByUuid(UUID uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM \"user\" WHERE status = 'ACTIVE'")
    List<UserEntity> getAllActiveUsers();
}