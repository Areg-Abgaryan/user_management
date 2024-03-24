/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses.user;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshTokenCreateResponse {

    @JsonProperty("uuid")
    @NotBlank private UUID uuid;

    @JsonProperty("token")
    @NotBlank private String token;
}
