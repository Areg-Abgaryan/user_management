/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses;

import com.areg.project.security.jwt.JwtToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {

    @NotBlank private String firstName;

    @NotBlank private String lastName;

    @NotBlank private JwtToken jwtToken;
}