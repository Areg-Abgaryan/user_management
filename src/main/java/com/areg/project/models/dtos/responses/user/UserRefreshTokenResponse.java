/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRefreshTokenResponse {

    @JsonProperty("jwt_token")
    @NotBlank private String jwtToken;

    @JsonProperty("refresh_token")
    @NotBlank private String refreshToken;


}
