/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.tokens;

import com.areg.project.builders.PermissionsWildcardBuilder;
import com.areg.project.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtProvider {

    private final String secret;
    private final long validTimeSeconds;
    private final PermissionsWildcardBuilder permissionsWildcardBuilder;

    @Autowired
    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expired}") long validTimeSeconds,
                       PermissionsWildcardBuilder permissionsWildcardBuilder) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validTimeSeconds = validTimeSeconds;
        this.permissionsWildcardBuilder = permissionsWildcardBuilder;
    }

    //  FIXME !! Get rid of deprecated api-s

    // Generate a new JWT
    public JwtToken createJwtToken(String email) {
        final Set<String> permissionsSet = permissionsWildcardBuilder.build(email);
        final String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date((Utils.getEpochSecondsNow() + validTimeSeconds) * 1000))
                .signWith(getSigningKey())
                .claim("permissions", permissionsSet)
                .compact();
        return JwtToken.create(token);
    }

    //  Is used from controllers for checking whether the user has permission for operation or not
    //  FIXME !! Test this
    public void checkPermissions(String jwtToken, String domain, String operation) {
        final List<String> permissions = getPermissionsFromToken(jwtToken);
        if (permissions.stream().noneMatch(perm -> perm.contains(domain) && perm.contains(operation))) {
            throw new AuthorizationException("You don't have permissions for this operation");
        }
    }

    // Validate the JWT token and expiration date
    public boolean isTokenValid(String token) {
        if (StringUtils.isBlank(token)) {
            throw new JwtException("Jwt token cannot be blank");
        }

        Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(token);
        return true;
    }

    private List<String> getPermissionsFromToken(String token) {
        try {
            final String permissionsJson = Jwts.parser()
                    .setSigningKey(getSigningKey()).build().parseSignedClaims(token)
                    .getPayload().get("permissions", String.class);

            if (StringUtils.isBlank(permissionsJson)) {
                throw new AuthorizationException("You don't have any permission for operations");
            }

            final var objectMapper = new ObjectMapper();
            return objectMapper.readValue(permissionsJson, new TypeReference<>() {});
        } catch (IOException e) {
            throw new AuthorizationException("Error while parsing permissions from the token", e);
        }
    }

    private SecretKey getSigningKey() {
        final byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
