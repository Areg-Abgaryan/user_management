/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRefreshTokenRequest {

    @JsonProperty("user_id")
    @NotBlank
    private UUID userId;
}
