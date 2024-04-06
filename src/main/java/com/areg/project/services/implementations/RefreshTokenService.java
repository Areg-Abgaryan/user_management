/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.services.implementations;

import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.SessionExpiredException;
import com.areg.project.exceptions.UserNotFoundException;
import com.areg.project.managers.EncryptionManager;
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
    public RefreshTokenService(@Value("${refresh.expired}") long validTimeSeconds, EncryptionManager encryptionManager,
                               UserService userService, IRefreshTokenRepository refreshTokenRepository) {
        this.validTimeSeconds = validTimeSeconds;
        this.encryptionManager = encryptionManager;
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    @Transactional
    public RefreshTokenEntity createRefreshToken(UUID userUuid) {
        //  Get the user from db with the specified id
        final UserEntity userEntity = userService.getActiveUserByUuid(userUuid);
        if (userEntity == null) {
            throw new UserNotFoundException(userUuid);
        }

        RefreshTokenEntity refreshTokenEntity = getByUserId(userEntity.getId());

        //  New refresh token flow
        if (refreshTokenEntity == null) {
            refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setUuid(UUID.randomUUID());
            refreshTokenEntity.setUserEntity(userEntity);
        }

        final long epochSecondsNow = Utils.getEpochSecondsNow();
        refreshTokenEntity.setCreatedAt(epochSecondsNow);
        fillDateFields(refreshTokenEntity, epochSecondsNow);
        fillCryptoFields(refreshTokenEntity);

        return refreshTokenRepository.saveAndFlush(refreshTokenEntity);
    }

    @Override
    @Transactional
    public RefreshTokenEntity updateRefreshToken(UUID userUuid) {
        //  Validate refresh token
        final RefreshTokenEntity refreshTokenEntity = validateRefreshToken(userUuid);

        fillDateFields(refreshTokenEntity, Utils.getEpochSecondsNow());
        fillCryptoFields(refreshTokenEntity);

        return refreshTokenRepository.saveAndFlush(refreshTokenEntity);
    }

    @Override
    public RefreshTokenEntity getByUserId(Long userId) {
        return refreshTokenRepository.findByUserEntityId(userId).orElse(null);
    }

    private RefreshTokenEntity validateRefreshToken(UUID userUuid) {
        //  Get the user from db with the specified id
        final UserEntity userEntity = userService.getActiveUserByUuid(userUuid);
        if (userEntity == null) {
            throw new UserNotFoundException(userUuid);
        }

        final RefreshTokenEntity refreshTokenEntity = getByUserId(userEntity.getId());
        if (refreshTokenEntity == null) {
            throw new ForbiddenOperationException("Could not find refresh token for user with id : " + userEntity.getUuid());
        }

        //  Has neither JWT token, nor refresh token (expired)
        if (isRefreshTokenExpired(refreshTokenEntity)) {
            throw new SessionExpiredException("Please log in again");
        }

        return refreshTokenEntity;
    }

    private boolean isRefreshTokenExpired(RefreshTokenEntity refreshTokenEntity) {
        final long currentTimestamp = Utils.getEpochSecondsNow();
        return refreshTokenEntity.getExpiringAt() <= currentTimestamp;
    }

    private void fillCryptoFields(RefreshTokenEntity refreshTokenEntity) {
        //  Generate salt and encrypt the token
        final UUID token = UUID.randomUUID();
        final String salt = encryptionManager.generateSalt();
        final String encryptedToken = encryptionManager.encrypt(token.toString(), salt);
        refreshTokenEntity.setSalt(salt);
        refreshTokenEntity.setToken(encryptedToken);

        //  Set token creation, update, and expiration dates
        final long epochNow = Utils.getEpochSecondsNow();
        refreshTokenEntity.setUpdatedAt(epochNow);
        refreshTokenEntity.setExpiringAt(epochNow + validTimeSeconds);
    }

    private void fillDateFields(RefreshTokenEntity refreshTokenEntity, long epochSecondsNow) {
        //  Set token creation, update, and expiration dates
        refreshTokenEntity.setUpdatedAt(epochSecondsNow);
        refreshTokenEntity.setExpiringAt(epochSecondsNow + validTimeSeconds);
    }
}
