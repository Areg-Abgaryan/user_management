/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repositories;

import com.areg.project.models.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserEntityId(Long userId);
}
