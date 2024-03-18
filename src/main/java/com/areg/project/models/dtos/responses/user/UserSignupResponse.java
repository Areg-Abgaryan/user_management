/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.dtos.responses.user;

import com.areg.project.managers.EmailVerificationManager;
import com.areg.project.models.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSignupResponse {

    @JsonProperty("id")
    @NotBlank private UUID id;

    @JsonProperty("email")
    @NotBlank @Email private String email;

    @JsonProperty("first_name")
    @NotBlank private String firstName;

    @JsonProperty("last_name")
    @NotBlank private String lastName;

    @JsonProperty("status")
    @NotBlank private UserStatus status;

    @JsonProperty("otp_verification_instructions")
    @NotBlank private String otpVerificationInstructions;

    public void setOtpVerificationInstructions(String email) {
        otpVerificationInstructions = EmailVerificationManager.createOTPInstructionsMessage(email);
    }
}