/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses.user;

import com.areg.project.security.tokens.JwtToken;
import com.areg.project.security.tokens.RefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenUpdateResponse {

    @JsonProperty("refresh_token")
    @NotBlank private RefreshToken refreshToken;

    @JsonProperty("jwt_token")
    @NotBlank private JwtToken jwtToken;
}