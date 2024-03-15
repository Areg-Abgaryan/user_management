/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.validators;

import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.OtpTimeoutException;
import com.areg.project.exceptions.WrongOtpException;
import com.areg.project.models.UserStatus;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
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
    public UserDataValidator(@Value("${otp.timeout.seconds}") int otpTimeoutSeconds, UserService userService) {
        this.otpTimeoutSeconds = otpTimeoutSeconds;
        this.userService = userService;
    }


    public void blockInactiveUserLogin(UserStatus userStatus, String email) {
        forbidAuthOperationForUser(userStatus, email);
    }

    public void blockVerifiedUserEmailVerification(UserStatus status, String email) {
        forbidAuthOperationForUser(status, email);
    }

    public void validateOtp(UserEntity entity, UserVerifyEmailDTO verifyEmailDto) {
        //  Get epoch seconds of the moment
        final long now = Utils.getEpochSecondsNow();

        //  Check otp creation time. Timeout if 120 seconds passed
        if (entity.getOtpCreationTime() + otpTimeoutSeconds < now) {
            userService.removeOtpData(entity);
            throw new OtpTimeoutException();
        }

        //  Check otp
        if (verifyEmailDto.getOtp() != entity.getOtp()) {
            throw new WrongOtpException();
        }
    }


    private void forbidAuthOperationForUser(UserStatus status, String email) {
        if (! status.equals(UserStatus.UNVERIFIED)) {
            final String exceptionMessage = switch (status) {
                case ACTIVE -> "The user with email " + email + " has already been verified";
                case DELETED -> "The user with email " + email + " is deleted";
                default -> "Invalid user status";
            };
            throw new ForbiddenOperationException(exceptionMessage);
        }
    }
}
