/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class UserGroupDTO {

    @JsonProperty("id")
    @NotBlank
    private UUID id;

    @JsonProperty("name")
    @NotBlank
    private String name;
}
