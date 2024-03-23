/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses.user;

import com.areg.project.models.enums.UserStatus;
import com.areg.project.security.jwt.JwtToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginResponse {

    @JsonProperty("uuid")
    @NotBlank private UUID uuid;

    @JsonProperty("first_name")
    @NotBlank private String firstName;

    @JsonProperty("last_name")
    @NotBlank private String lastName;

    @JsonProperty("user_status")
    @NotBlank private UserStatus status;

    @JsonProperty("jwt_token")
    @NotBlank private JwtToken jwtToken;
}