/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.managers.EncryptionManager;
import com.areg.project.models.dtos.requests.user.RefreshTokenRequest;
import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.repositories.IRefreshTokenRepository;
import com.areg.project.services.interfaces.IRefreshTokenService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    private final long validTimeSeconds;

    private final EncryptionManager encryptionManager;
    private final UserService userService;
    private final IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(@Value("${jwt.expired}") long validTimeSeconds, EncryptionManager encryptionManager,
                               UserService userService, IRefreshTokenRepository refreshTokenRepository) {
        this.validTimeSeconds = validTimeSeconds;
        this.encryptionManager = encryptionManager;
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    @Transactional
    public RefreshTokenEntity createRefreshToken(UUID userUuid) {

        final UserEntity userEntity = userService.getActiveUserByUuid(userUuid);
        RefreshTokenEntity refreshTokenEntity = getByUserEntityId(userEntity.getId());

        //  New refresh token flow
        if (refreshTokenEntity == null) {
            refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setUuid(UUID.randomUUID());
            refreshTokenEntity.setUserEntity(userEntity);
        }

        //  Generate salt and encrypt the token
        final UUID token = UUID.randomUUID();
        final String salt = encryptionManager.generateSalt();
        final String encryptedToken = encryptionManager.encrypt(token.toString(), salt);
        refreshTokenEntity.setSalt(salt);
        refreshTokenEntity.setToken(encryptedToken);

        //  Set token creation, update, and expiration dates
        final long epochNow = Utils.getEpochSecondsNow();
        refreshTokenEntity.setCreatedAt(epochNow);
        refreshTokenEntity.setUpdatedAt(epochNow);
        refreshTokenEntity.setExpiringAt(epochNow + validTimeSeconds);

        return refreshTokenRepository.saveAndFlush(refreshTokenEntity);
    }

    @Override
    public RefreshTokenEntity getByUserEntityId(Long userId) {
        return refreshTokenRepository.findByUserEntityId(userId).orElse(null);
    }
}
