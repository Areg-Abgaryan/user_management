/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.requests.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginRequest {

    @JsonProperty("email")
    @NotBlank
    private String email;

    @JsonProperty("password")
    @NotBlank private String password;
}
