/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtToken {

    @JsonProperty("token")
    private String token;

    public static JwtToken create(String token) {
        return new JwtToken(token);
    }
}