/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.builders.UserPermissionsWildcardBuilder;
import com.areg.project.converters.UserConverter;
import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.OtpTimeoutException;
import com.areg.project.exceptions.WrongOtpException;
import com.areg.project.models.UserStatus;
import com.areg.project.models.dtos.UserDTO;
import com.areg.project.models.dtos.requests.user.UserLoginDTO;
import com.areg.project.models.dtos.requests.user.UserSignUpDTO;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
import com.areg.project.models.dtos.responses.user.UserLoginResponse;
import com.areg.project.models.dtos.responses.user.UserSignupResponse;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.security.jwt.JwtProvider;
import com.areg.project.security.jwt.JwtToken;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import com.areg.project.validators.UserInputValidator;
import jakarta.mail.internet.AddressException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserManager {

    private static String exceptionMessage;

    private final UserService userService;
    private final UserInputValidator userInputValidator;
    private final UserConverter userConverter;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;
    private final UserPermissionsWildcardBuilder userPermissionsWildcardBuilder;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserManager(UserService userService, UserInputValidator userInputValidator, UserConverter userConverter,
                       EncryptionManager encryptionManager, EmailVerificationManager emailVerificationManager,
                       UserPermissionsWildcardBuilder userPermissionsWildcardBuilder, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userInputValidator = userInputValidator;
        this.userConverter = userConverter;
        this.encryptionManager = encryptionManager;
        this.emailVerificationManager = emailVerificationManager;
        this.userPermissionsWildcardBuilder = userPermissionsWildcardBuilder;
        this.jwtProvider = jwtProvider;
    }


    public UserSignupResponse createUnverifiedUser(UserSignUpDTO signUpDto) throws AddressException {
        //  Validate user input
        userInputValidator.validateUserInput(signUpDto);

        final UserEntity entity = userConverter.fromSignUpDtoToEntity(signUpDto);
        fillSignUpEntityFields(entity, signUpDto.getPassword());

        //  FIXME !! Validate when the user with that email already exists
        //  Save unverified user
        final UserEntity savedEntity = userService.createUnverifiedUser(entity);

        //  Convert to response type
        final UserSignupResponse savedResponse = userConverter.fromEntityToSignUpResponse(savedEntity);

        //  Set otp verification instructions
        savedResponse.setOtpVerificationInstructions(savedResponse.getEmail());

        //  Send one time password to the destination email
        emailVerificationManager.sendEmail(signUpDto.getEmail(), entity.getOtp());

        return savedResponse;
    }

    //  FIXME !! We have a bug for this scenario : When user signs up but can't succeed to enter otp in time, he can't verify himself at all, we don't have logic here
    public UserSignupResponse verifyUserEmail(UserVerifyEmailDTO verifyEmailDto) throws AddressException {
        //  Validate user input
        userInputValidator.validateUserInput(verifyEmailDto);

        //  Get epoch seconds of the moment
        final long now = Utils.getEpochSecondsNow();

        //  Get the user with specified email
        final String verifyDtoEmail = verifyEmailDto.getEmail();
        final UserEntity entity = userService.findUserByEmail(verifyDtoEmail);

        //  Check whether the user has already been verified or not
        final UserStatus status = entity.getStatus();
        if (! status.equals(UserStatus.UNVERIFIED)) {
            switch (status) {
                case ACTIVE -> exceptionMessage = "The user with email " + verifyDtoEmail + " has already been verified";
                case DELETED -> exceptionMessage = "The user with email " + verifyDtoEmail + " is deleted";
            }
            throw new ForbiddenOperationException(exceptionMessage);
        }

        //  Check whether the specified password is correct
        if (! entity.getPassword().equals(encryptionManager.encrypt(verifyEmailDto.getPassword(), entity.getSalt()))) {
            throw new AuthenticationException("Invalid password for user " + entity.getEmail());
        } else {
            //  Check otp creation time. Timeout if 120 seconds passed
            if (entity.getOtpCreationTime() + 120 < now) {
                userService.removeOtpData(entity);
                throw new OtpTimeoutException();
            }

            //  Check otp
            if (verifyEmailDto.getOtp() != entity.getOtp()) {
                userService.removeOtpData(entity);
                throw new WrongOtpException();
            }

            //  Save the updated user
            final UserEntity savedEntity = userService.saveVerifiedUser(entity);

            return userConverter.fromEntityToSignUpResponse(savedEntity);
        }
    }

    public UserLoginResponse login(UserLoginDTO loginDto) throws AddressException {
        //  Validate user input
        userInputValidator.validateUserInput(loginDto);

        final String email = loginDto.getEmail();

        //  FIXME !! I don't think this is a good way. I'd be better to have 2 implementations of UserService, for active users and for all ones
        //  Disable logging in for unverified users
        final UserSignupResponse userResponse = findUserByEmail(email);
        final UserStatus status = userResponse.getStatus();
        if (! status.equals(UserStatus.ACTIVE)) {
            switch (status) {
                case UNVERIFIED -> exceptionMessage = "Access denied. User with email '" + email + "' is not verified";
                case DELETED -> exceptionMessage = "Access denied. User with email '" + email + "' is not active";
            }
            throw new ForbiddenOperationException(exceptionMessage);
        }

        //  Log in and update last log in time
        final var token = new UsernamePasswordToken(email, loginDto.getPassword());
        SecurityUtils.getSubject().login(token);

        //  Update user's last log in time
        updateLastLoginTime(email, Utils.getCurrentDateAndTime());

        // Generate JWT token with user permissions
        final Set<String> permissionsSet = userPermissionsWildcardBuilder.buildPermissionsWildcards(userResponse.getId());
        final String jwtTokenString = jwtProvider.createJwtToken(email, permissionsSet);
        final JwtToken jwtToken = JwtToken.create(jwtTokenString);

        return new UserLoginResponse(userResponse.getFirstName(), userResponse.getLastName(), userResponse.getStatus(), jwtToken);
    }

    //  FIXME !! Return only ACTIVE users to api-s
    public UserSignupResponse findUserByEmail(String email) {
        final UserEntity entity = userService.findUserByEmail(email);
        return userConverter.fromEntityToSignUpResponse(entity);
    }

    public void updateLastLoginTime(String email, LocalDateTime loginDate) {
        userService.updateLastLoginTime(email, loginDate);
    }


    private void fillSignUpEntityFields(UserEntity entity, String password) {
        //  Set salt
        final String salt = encryptionManager.generateSalt();
        entity.setSalt(salt);

        //  Set encrypted password
        final String encryptedPassword = encryptionManager.encrypt(password, salt);
        entity.setPassword(encryptedPassword);

        //  Create one time password and set to the user entity
        final int otp = emailVerificationManager.generateOneTimePassword();
        entity.setOtp(otp);
        final long otpCreationTime = Utils.getEpochSecondsNow();
        entity.setOtpCreationTime(otpCreationTime);
    }

    public Set<UserDTO> getAllActiveUsers() {
        return userConverter.fromEntityToDto(userService.getAllActiveUsers());
    }
}