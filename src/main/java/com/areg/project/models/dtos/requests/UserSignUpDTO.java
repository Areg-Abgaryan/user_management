/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserSignUpDTO {

    @JsonProperty("email")
    @NotBlank @Email private String email;

    @JsonProperty("password")
    @NotBlank private String password;

    @JsonProperty("firstName")
    @NotBlank private String firstName;

    @JsonProperty("lastName")
    @NotBlank private String lastName;
}