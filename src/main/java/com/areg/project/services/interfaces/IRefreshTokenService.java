/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.interfaces;

import com.areg.project.models.entities.RefreshTokenEntity;

public interface IRefreshTokenService {

    RefreshTokenEntity getRefreshTokenByUserId(Long userId);
}
