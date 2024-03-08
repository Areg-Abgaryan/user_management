/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtProvider {

    private final String secret;
    private final long validTimeMillis;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    public JwtProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expired}") long validTimeMillis,
                       JwtUserDetailsService jwtUserDetailsService) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.validTimeMillis = validTimeMillis;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    // Generate a new JWT
    public String createJwtToken(String email, Set<String> setOfPermissions) {
        final Claims claims = (Claims) Jwts.claims().subject(email);
        claims.put("perms", setOfPermissions);

        return Jwts.builder().claims(claims).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validTimeMillis))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // Validate the JWT token
    public boolean isTokenValid(String token) {
        try {
            if (StringUtils.isBlank(token)) {
                throw new JwtException("Token is invalid !");
            }
            final Jws<Claims> claims = Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token);
            return claims.getPayload().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(getEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return bearerToken != null && bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : null;
    }

    public void checkPermissions(String jwtToken, String domain, String operation) {
        final List<String> permissionsFromToken = getPermissionsFromToken(jwtToken);
        if (permissionsFromToken.stream().noneMatch(perm -> perm.contains(domain) && !perm.contains(operation))) {
            throw new AuthorizationException("You don't have permissions for this operation");
        }
    }

    private List<String> getPermissionsFromToken(String token) {
        final List<String> perms = (List<String>) Jwts.parser()
                .setSigningKey(secret).build()
                .parseSignedClaims(token)
                .getPayload().get("perms");
        if (CollectionUtils.isEmpty(perms)) {
            throw new AuthorizationException("You don't have any permission for operations");
        }
        return perms;
    }

    private String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
}
