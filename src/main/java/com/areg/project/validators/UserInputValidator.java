/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.validators;

import com.areg.project.exceptions.BlankInputDataException;
import com.areg.project.managers.EmailVerificationManager;
import com.areg.project.models.dtos.requests.user.UserLoginDTO;
import com.areg.project.models.dtos.requests.user.UserSignUpDTO;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
import jakarta.mail.internet.AddressException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInputValidator {

    private final InputPatternValidator inputPatternValidator;
    private final EmailVerificationManager emailVerificationManager;

    @Autowired
    public UserInputValidator(InputPatternValidator inputPatternValidator, EmailVerificationManager emailVerificationManager) {
        this.inputPatternValidator = inputPatternValidator;
        this.emailVerificationManager = emailVerificationManager;
    }

    //  Check whether the user input is valid or not
    //  FIXME !! Validate password, firstName, lastName
    public void validateUserInput(UserSignUpDTO signUpDto) throws AddressException {
        if (StringUtils.isAnyBlank(signUpDto.getEmail(), signUpDto.getPassword(), signUpDto.getFirstName(),
                signUpDto.getLastName())) {
            throw new BlankInputDataException();
        }
        emailVerificationManager.isValidEmailAddress(signUpDto.getEmail());
    }
    public void validateUserInput(UserVerifyEmailDTO verifyEmailDto) throws AddressException {
        if (StringUtils.isAnyBlank(verifyEmailDto.getEmail(), verifyEmailDto.getPassword())) {
            throw new BlankInputDataException();
        }
        emailVerificationManager.isValidEmailAddress(verifyEmailDto.getEmail());
    }
    public void validateUserInput(UserLoginDTO loginDto) throws AddressException {
        if (StringUtils.isAnyBlank(loginDto.getEmail(), loginDto.getPassword())) {
            throw new BlankInputDataException();
        }
        emailVerificationManager.isValidEmailAddress(loginDto.getEmail());
    }
}
