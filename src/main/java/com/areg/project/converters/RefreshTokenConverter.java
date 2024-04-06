/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.security.tokens.RefreshToken;
import com.areg.project.utils.Utils;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenConverter {

    public RefreshTokenCreateResponse fromEntityToCreateResponse(RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        final var refreshTokenResponse = new RefreshTokenCreateResponse();
        refreshTokenResponse.setToken(entity.getToken());
        refreshTokenResponse.setUuid(entity.getUuid());
        return refreshTokenResponse;
    }

    public RefreshToken fromEntityToToken(RefreshTokenEntity refreshTokenEntity) {
        if (refreshTokenEntity == null) {
            return null;
        }

        final var refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenEntity.getToken());
        refreshToken.setUuid(refreshTokenEntity.getUuid());
        refreshToken.setUserUuid(refreshTokenEntity.getUserEntity().getUuid());
        refreshToken.setExpiringAt(Utils.epochSecondsToLocalDateTime(refreshTokenEntity.getExpiringAt()));
        return refreshToken;
    }
}
