/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos;

import com.areg.project.models.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class UserDTO {

    @JsonProperty("id")
    @NotBlank private UUID id;

    @JsonProperty("email")
    @NotBlank @Email
    private String email;

    @JsonProperty("first_name")
    @NotBlank
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank private String lastName;

    @JsonProperty("status")
    @NotBlank private UserStatus status;

    @JsonProperty("created")
    @NotBlank private LocalDateTime created;

    @JsonProperty("updated")
    @NotBlank private LocalDateTime updated;

    @JsonProperty("last_login")
    @NotBlank private LocalDateTime lastLoginTime;
}
