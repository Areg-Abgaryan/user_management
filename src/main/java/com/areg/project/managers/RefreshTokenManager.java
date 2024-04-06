/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.RefreshTokenConverter;
import com.areg.project.exceptions.BlankInputDataException;
import com.areg.project.models.dtos.requests.user.RefreshTokenRequest;
import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.dtos.responses.user.RefreshTokenUpdateResponse;
import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.security.tokens.JwtProvider;
import com.areg.project.security.tokens.JwtToken;
import com.areg.project.security.tokens.RefreshToken;
import com.areg.project.services.implementations.RefreshTokenService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenManager {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenConverter refreshTokenConverter;

    @Autowired
    public RefreshTokenManager(JwtProvider jwtProvider, RefreshTokenService refreshTokenService,
                               RefreshTokenConverter refreshTokenConverter) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenConverter = refreshTokenConverter;
    }


    //  Create refresh token
    public RefreshTokenCreateResponse createRefreshToken(UUID userUuid) {
        final RefreshTokenEntity tokenEntity = refreshTokenService.createRefreshToken(userUuid);
        return refreshTokenConverter.fromEntityToCreateResponse(tokenEntity);
    }

    //  Update existing refresh token
    public RefreshTokenUpdateResponse updateRefreshToken(RefreshTokenRequest request) {
        //  Validate input refresh token request data
        if (request == null || request.getUserUuid() == null) {
            throw new BlankInputDataException();
        }

        //  Generate new refresh token
        final RefreshTokenEntity updatedRefreshToken = refreshTokenService.updateRefreshToken(request.getUserUuid());
        final RefreshToken refreshToken = refreshTokenConverter.fromEntityToToken(updatedRefreshToken);

        //  Generate new JWT token
        final JwtToken jwtToken = jwtProvider.createJwtToken(updatedRefreshToken.getUserEntity().getEmail());

        return new RefreshTokenUpdateResponse(refreshToken, jwtToken);
    }
}
