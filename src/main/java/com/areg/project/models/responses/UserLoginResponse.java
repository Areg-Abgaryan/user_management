/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {

    @NotBlank private UUID id;

    @NotBlank @Email private String email;

    @NotBlank private String firstName;

    @NotBlank private String lastName;

    @NotBlank private String jwtToken;
}