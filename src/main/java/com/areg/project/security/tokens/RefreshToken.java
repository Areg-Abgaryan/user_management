/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.tokens;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshToken {

    @JsonProperty("uuid")
    @NotBlank private UUID uuid;

    @JsonProperty("token")
    @NotBlank private String token;

    @JsonProperty("user_uuid")
    @NotBlank private UUID userUuid;

    @JsonProperty("expiring_at")
    @NotBlank private LocalDateTime expiringAt;
}
