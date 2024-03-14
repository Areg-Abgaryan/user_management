/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.builders.UserPermissionsWildcardBuilder;
import com.areg.project.converters.UserConverter;
import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.OtpTimeoutException;
import com.areg.project.exceptions.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserManager {

    private static String exceptionMessage;

    private final int otpTimeoutSeconds;

    private final UserService userService;
    private final UserInputValidator userInputValidator;
    private final UserConverter userConverter;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;
    private final UserPermissionsWildcardBuilder userPermissionsWildcardBuilder;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserManager(@Value("${otp.timeout.seconds}") int otpTimeoutSeconds, UserService userService, UserInputValidator userInputValidator, UserConverter userConverter,
                       EncryptionManager encryptionManager, EmailVerificationManager emailVerificationManager,
                       UserPermissionsWildcardBuilder userPermissionsWildcardBuilder, JwtProvider jwtProvider) {
        this.otpTimeoutSeconds = otpTimeoutSeconds;
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

        //  Check whether the user already has tried to sign up
        UserEntity getOrCreateUserEntity = userService.getUserByEmail(signUpDto.getEmail());
        final UserEntity entity = userConverter.fromSignUpDtoToEntity(signUpDto);

        //  Create a new user in the system
        if (getOrCreateUserEntity == null) {
            //  Convert dto to entity and set fields
            fillSignUpEntityCryptoFields(entity, signUpDto.getPassword());

            //  Save unverified user
            getOrCreateUserEntity = userService.createUnverifiedUser(entity);

            return processUserAndSendEmail(getOrCreateUserEntity);
        }

        switch (getOrCreateUserEntity.getStatus()) {
            case UNVERIFIED -> {
                //  Create a new otp
                fillOtpFields(getOrCreateUserEntity);
                return processUserAndSendEmail(getOrCreateUserEntity);
            }
            case DELETED -> throw new ForbiddenOperationException("The user is deleted from the system");
            case ACTIVE -> throw new ForbiddenOperationException("The user is signed up and verified, go to log in");
            default -> throw new IllegalArgumentException();
        }
    }

    public UserSignupResponse verifyUserEmail(UserVerifyEmailDTO verifyEmailDto) throws AddressException {
        //  Validate user input
        userInputValidator.validateUserInput(verifyEmailDto);

        //  Get epoch seconds of the moment
        final long now = Utils.getEpochSecondsNow();

        //  Get the user with specified email
        final String verifyDtoEmail = verifyEmailDto.getEmail();
        final UserEntity entity = userService.getUserByEmail(verifyDtoEmail);

        //  Check whether the user exists in the system
        if (entity == null) {
            throw new UserNotFoundException(verifyDtoEmail);
        }

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
            if (entity.getOtpCreationTime() + otpTimeoutSeconds < now) {
                userService.removeOtpData(entity);
                throw new OtpTimeoutException();
            }

            //  Check otp
            if (verifyEmailDto.getOtp() != entity.getOtp()) {
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

        //  FIXME !! I don't think this is a good way. Have 2 implementations of UserService, for active users and for all ones ?

        //  Check whether the user exists in the system
        final UserSignupResponse userResponse = getUserByEmail(email);
        if (userResponse == null) {
            throw new UserNotFoundException(email);
        }

        //  Disable logging in for unverified users
        final UserStatus status = userResponse.getStatus();
        if (! status.equals(UserStatus.ACTIVE)) {
            switch (status) {
                case UNVERIFIED -> exceptionMessage = "User with email '" + email + "' is not verified";
                case DELETED -> exceptionMessage = "User with email '" + email + "' is not active";
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
    public UserSignupResponse getUserByEmail(String email) {
        final UserEntity entity = userService.getUserByEmail(email);
        return userConverter.fromEntityToSignUpResponse(entity);
    }

    public void updateLastLoginTime(String email, LocalDateTime loginDate) {
        userService.updateLastLoginTime(email, loginDate);
    }

    public Set<UserDTO> getAllActiveUsers() {
        return userConverter.fromEntityToDto(userService.getAllActiveUsers());
    }


    private UserSignupResponse processUserAndSendEmail(UserEntity userEntity) {
        //  Convert to response type
        final UserSignupResponse signUpResponse = userConverter.fromEntityToSignUpResponse(userEntity);

        //  Set otp verification instructions
        signUpResponse.setOtpVerificationInstructions(signUpResponse.getEmail());

        //  Send one time password to the destination email
        emailVerificationManager.sendEmail(userEntity.getEmail(), userEntity.getOtp());

        return signUpResponse;
    }

    private void fillSignUpEntityCryptoFields(UserEntity entity, String password) {
        if (entity == null) {
            return;
        }

        //  Set salt
        final String salt = encryptionManager.generateSalt();
        entity.setSalt(salt);

        //  Set encrypted password
        final String encryptedPassword = encryptionManager.encrypt(password, salt);
        entity.setPassword(encryptedPassword);

        fillOtpFields(entity);
    }

    private void fillOtpFields(UserEntity entity) {
        //  Create one time password and set to the user entity
        final int otp = emailVerificationManager.generateOneTimePassword();
        entity.setOtp(otp);

        final long otpCreationTime = Utils.getEpochSecondsNow();
        entity.setOtpCreationTime(otpCreationTime);
        userService.updateUser(entity);
    }
}