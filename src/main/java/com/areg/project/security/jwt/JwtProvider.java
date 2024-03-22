/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import com.areg.project.builders.PermissionsWildcardBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
    private final long validTimeMillis;
    private final PermissionsWildcardBuilder permissionsWildcardBuilder;

    @Autowired
    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expired}") long validTimeMillis,
                       PermissionsWildcardBuilder permissionsWildcardBuilder) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validTimeMillis = validTimeMillis;
        this.permissionsWildcardBuilder = permissionsWildcardBuilder;
    }

    //  FIXME !! Get rid of deprecated api-s

    // Generate a new JWT
    public JwtToken createJwtToken(String email) {
        final Set<String> permissionsSet = permissionsWildcardBuilder.build(email);
        final String token = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validTimeMillis))
                .signWith(getSigningKey())
                .claim("permissions", permissionsSet)
                .compact();
        return JwtToken.create(token);
    }

    //  Is used from controllers for checking whether the user has permission for operation or not
    //  FIXME !! Test this
    public void checkPermissions(String jwtToken, String domain, String operation) {
        final List<String> permissions = getPermissionsFromToken(jwtToken);
        if (permissions.stream().noneMatch(perm -> perm.contains(domain) && !perm.contains(operation))) {
            throw new AuthorizationException("You don't have permissions for this operation");
        }
    }

    // Validate the JWT token
    public boolean isTokenValid(String token) {
        //  Check whether the token is empty
        if (StringUtils.isBlank(token)) {
            throw new JwtException("Jwt token cannot be blank");
        }

        final Jws<Claims> claims = Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(token);

        // Verify nullability and expiration time
        return claims != null && claims.getPayload() != null && claims.getPayload().getExpiration() != null &&
                !claims.getPayload().getExpiration().before(new Date());
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
