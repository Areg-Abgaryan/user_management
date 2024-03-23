/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.repositories.IRefreshTokenRepository;
import com.areg.project.services.interfaces.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    public RefreshTokenEntity getRefreshTokenByUserId(Long userId) {
        return refreshTokenRepository.findByUserEntityId(userId).orElse(null);
    }

}
