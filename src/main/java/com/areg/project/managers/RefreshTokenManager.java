/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.RefreshTokenConverter;
import com.areg.project.models.dtos.requests.user.RefreshTokenRequest;
import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.dtos.responses.user.RefreshTokenUpdateResponse;
import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.security.jwt.JwtProvider;
import com.areg.project.services.implementations.RefreshTokenService;
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
    public RefreshTokenUpdateResponse updateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
        //  FIXME !!
        //final Optional<RefreshTokenEntity> token = refreshTokenRepository.findByUserEntityAndRefreshTokenId(userEntity, refreshTokenInputDto.getRefreshToken());

        //  Has neither JWT token, nor refresh token (expired)
        //if (token.isEmpty() || authManager.isRefreshTokenExpired(token.get())) {
        //    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Войдите в систему заново");
        //}

        //final UserEntity user = token.get().getUserEntity();

        //  Generate new JWT token
        //return jwtProvider.createJwtToken(email);
        return null;
    }
}
