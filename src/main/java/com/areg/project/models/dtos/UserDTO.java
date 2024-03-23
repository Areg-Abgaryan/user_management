/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos;

import com.areg.project.models.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    @JsonProperty("uuid")
    @NotBlank private UUID uuid;

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

    @JsonProperty("creation_date")
    @NotBlank private LocalDateTime creationDate;

    @JsonProperty("update_date")
    @NotBlank private LocalDateTime updateDate;

    @JsonProperty("last_login")
    @NotBlank private LocalDateTime lastLoginDate;
}
