/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.RefreshTokenEntity;

import java.util.UUID;

public interface IRefreshTokenService {

    RefreshTokenEntity createRefreshToken(UUID userUuid);

    RefreshTokenEntity updateRefreshToken(UUID userUuid);

    RefreshTokenEntity getByUserId(Long userId);
}
