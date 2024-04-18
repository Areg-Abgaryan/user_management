/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.entities.RefreshTokenEntity;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.security.tokens.RefreshToken;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.*;

public class RefreshTokenConverterTest {

    private UUID testTokenUuid, testUserUuid;
    private String testToken;
    private RefreshTokenConverter converter;

    @BeforeMethod
    public void setUp() {
        converter = new RefreshTokenConverter();
        testTokenUuid = UUID.randomUUID();
        testUserUuid = UUID.randomUUID();
        testToken = "testToken";
    }

    @Test(priority = 1)
    public void testFromEntityToCreateResponse_NullEntity() {
        // Test conversion from null entity to CreateResponse
        final RefreshTokenCreateResponse response = converter.fromEntityToCreateResponse(null);
        assertNull(response); // Expecting null response
    }

    @Test(priority = 2)
    public void testFromEntityToCreateResponse_NonNullEntity() {
        // Test conversion from non-null entity to CreateResponse
        final var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(testToken);
        refreshTokenEntity.setUuid(testTokenUuid);

        //  Convert and test
        final RefreshTokenCreateResponse response = converter.fromEntityToCreateResponse(refreshTokenEntity);
        assertNotNull(response);
        assertEquals(testToken, response.getToken());
        assertEquals(testTokenUuid, response.getUuid());
    }

    @Test
    public void testFromEntityToToken_NullEntity() {
        // Test conversion from null entity to Token
        final RefreshToken token = converter.fromEntityToToken(null);
        assertNull(token);
    }

    @Test(priority = 3)
    public void testFromEntityToToken_NonNullEntity() {
        // Test conversion from non-null entity to Token
        final RefreshToken token = converter.fromEntityToToken(buildRefreshToken());
        assertNotNull(token);
        assertEquals(testToken, token.getToken());
        assertEquals(testTokenUuid, token.getUuid());
        assertEquals(testUserUuid, token.getUserUuid());
    }


    private RefreshTokenEntity buildRefreshToken() {
        final var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(testToken);
        refreshTokenEntity.setUuid(testTokenUuid);

        final var userEntity = new UserEntity();
        userEntity.setUuid(testUserUuid);

        refreshTokenEntity.setUserEntity(userEntity);
        refreshTokenEntity.setExpiringAt(123456789L);

        return refreshTokenEntity;
    }
}
