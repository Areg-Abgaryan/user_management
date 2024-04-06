/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class JwtToken {

    @JsonProperty("token")
    private String token;

    public static JwtToken create(String token) {
        return new JwtToken(token);
    }
}