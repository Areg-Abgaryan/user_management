/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.validators;

import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.InvalidOtpException;
import com.areg.project.models.enums.UserStatus;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailRequest;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidator {

    private final int otpTimeoutSeconds;
    private final UserService userService;

    @Autowired
    public UserDataValidator(@Value("${otp.timeout}") int otpTimeoutSeconds, UserService userService) {
        this.otpTimeoutSeconds = otpTimeoutSeconds;
        this.userService = userService;
    }


    //  Disable logging in for users that do not have 'ACTIVE' status
    public void blockInactiveUserLogin(UserStatus status, String email) {
        if (! status.equals(UserStatus.ACTIVE)) {
            final String exceptionMessage = switch (status) {
                case UNVERIFIED -> "The user with email " + email + " is not verified";
                case DELETED -> "The user with email " + email + " is deleted";
                default -> "Invalid user status";
            };
            throw new ForbiddenOperationException(exceptionMessage);
        }
    }

    //  Disable email verifying for users that do not have 'UNVERIFIED' status
    public void blockVerifiedUserEmailVerification(UserStatus status, String email) {
        if (! status.equals(UserStatus.UNVERIFIED)) {
            final String exceptionMessage = switch (status) {
                case ACTIVE -> "The user with email " + email + " has already been verified";
                case DELETED -> "The user with email " + email + " is deleted";
                default -> "Invalid user status";
            };
            throw new ForbiddenOperationException(exceptionMessage);
        }
    }

    public void validateOtp(UserEntity entity, UserVerifyEmailRequest verifyEmailDto) {
        //  Get epoch seconds of the moment
        final long now = Utils.getEpochSecondsNow();

        //  Check otp creation time. Timeout if 120 seconds passed
        if (entity.getOtpCreationTime() + otpTimeoutSeconds < now) {
            userService.removeOtpData(entity);
            throw new InvalidOtpException("One time password input timeout");
        }

        //  Check otp
        if (verifyEmailDto.getOtp() != entity.getOtp()) {
            throw new InvalidOtpException("Wrong one time password provided");
        }
    }
}
