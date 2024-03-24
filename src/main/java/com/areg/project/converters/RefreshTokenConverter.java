/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.entities.RefreshTokenEntity;
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
}
