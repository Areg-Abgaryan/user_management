/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.UserConverter;
import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.UserNotFoundException;
import com.areg.project.models.dtos.requests.user.UserLoginRequest;
import com.areg.project.models.dtos.requests.user.RefreshTokenRequest;
import com.areg.project.models.dtos.requests.user.UserSignUpRequest;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailRequest;
import com.areg.project.models.dtos.responses.user.RefreshTokenUpdateResponse;
import com.areg.project.models.dtos.responses.user.UserLoginResponse;
import com.areg.project.models.dtos.responses.user.RefreshTokenCreateResponse;
import com.areg.project.models.dtos.responses.user.UserSignupResponse;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.security.tokens.JwtProvider;
import com.areg.project.security.tokens.JwtToken;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import com.areg.project.validators.UserDataValidator;
import com.areg.project.validators.UserInputValidator;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.internet.AddressException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthManager {

    private final JwtProvider jwtProvider;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;

    private final UserInputValidator userInputValidator;
    private final UserDataValidator userDataValidator;
    private final UserManager userManager;
    private final UserService userService;
    private final UserConverter userConverter;
    private final RefreshTokenManager refreshTokenManager;

    @Autowired
    public AuthManager(JwtProvider jwtProvider, EncryptionManager encryptionManager,
                       EmailVerificationManager emailVerificationManager, UserManager userManager,
                       UserService userService, UserInputValidator userInputValidator,
                       UserDataValidator userDataValidator, UserConverter userConverter,
                       RefreshTokenManager refreshTokenManager) {
        this.jwtProvider = jwtProvider;
        this.encryptionManager = encryptionManager;
        this.emailVerificationManager = emailVerificationManager;
        this.userManager = userManager;
        this.userService = userService;
        this.userInputValidator = userInputValidator;
        this.userDataValidator = userDataValidator;
        this.userConverter = userConverter;
        this.refreshTokenManager = refreshTokenManager;
    }

    public UserSignupResponse createUnverifiedUser(UserSignUpRequest signUpDto) throws AddressException {
        //  Validate user input data
        userInputValidator.validateUserInput(signUpDto);

        //  Check whether the user already has tried to sign up
        UserEntity entity = userService.getUserByEmail(signUpDto.getEmail());

        if (entity == null) {
            //  Convert from dto, set crypto fields, create a new user in the system and send otp email
            entity = userConverter.fromRequestToEntity(signUpDto);
            fillSignUpEntityCryptoFields(entity, signUpDto.getPassword());
            return processUserAndSendEmail(userService.createUnverifiedUser(entity));
        }

        switch (entity.getStatus()) {
            case UNVERIFIED -> {
                //  Create a new otp
                fillOtpFields(entity);
                return processUserAndSendEmail(entity);
            }
            case DELETED -> throw new ForbiddenOperationException("The user is deleted from the system");
            case ACTIVE -> throw new ForbiddenOperationException("The user is signed up and verified, go to log in");
            default -> throw new IllegalArgumentException();
        }
    }

    public UserSignupResponse verifyUserEmail(UserVerifyEmailRequest verifyEmailDto) throws AddressException {
        //  Validate user input data
        userInputValidator.validateUserInput(verifyEmailDto);

        //  Get the user with specified email
        final String verifyDtoEmail = verifyEmailDto.getEmail();
        final UserEntity entity = userService.getUserByEmail(verifyDtoEmail);

        //  Check whether the user exists in the system
        if (entity == null) {
            throw new UserNotFoundException(verifyDtoEmail);
        }

        userDataValidator.blockVerifiedUserEmailVerification(entity.getStatus(), entity.getEmail());

        //  Check whether the specified password is correct
        if (! entity.getPassword().equals(encryptionManager.encrypt(verifyEmailDto.getPassword(), entity.getSalt()))) {
            throw new AuthenticationException("Invalid password for user " + entity.getEmail());
        }

        userDataValidator.validateOtp(entity, verifyEmailDto);

        //  Save the updated user
        final UserEntity savedEntity = userService.saveVerifiedUser(entity);
        return userConverter.fromEntityToSignUpResponse(savedEntity);
    }

    public UserLoginResponse login(UserLoginRequest loginDto) throws AddressException {
        //  Validate user input data
        userInputValidator.validateUserInput(loginDto);

        final String email = loginDto.getEmail();

        //  Check whether the user exists in the system
        final UserEntity userEntity = userService.getUserByEmail(email);
        if (userEntity == null) {
            throw new UserNotFoundException(email);
        }

        //  Validate user's status
        userDataValidator.blockInactiveUserLogin(userEntity.getStatus(), email);

        //  Log in and update last log in time
        final var token = new UsernamePasswordToken(email, loginDto.getPassword());
        SecurityUtils.getSubject().login(token);

        //  Update user's last log in date
        userManager.updateLastLoginDate(email);

        // Generate JWT token with user permissions wildcard
        final JwtToken jwtToken = jwtProvider.createJwtToken(email);

        //  Generate refresh token for the user
        final RefreshTokenCreateResponse refreshToken = refreshTokenManager.createRefreshToken(userEntity.getUuid());

        return new UserLoginResponse(userEntity.getUuid(), userEntity.getFirstName(), userEntity.getLastName(),
                userEntity.getStatus(), jwtToken, refreshToken);
    }

    //  FIXME !! There is a bug, when the user is logged out, he still can access other api-s
    public void logout(String jwtToken) {
        if (jwtProvider.isTokenValid(jwtToken)) {
            SecurityUtils.getSubject().logout();
        } else {
            throw new AuthenticationException("Could not log out");
        }
    }

    public RefreshTokenUpdateResponse refreshToken(RefreshTokenRequest refreshTokenRequest, String jwtToken) {
        try {
            if (jwtProvider.isTokenValid(jwtToken)) {
                throw new ForbiddenOperationException("Jwt token is not expired yet");
            }
        } catch (ExpiredJwtException e) {
            return refreshTokenManager.updateRefreshToken(refreshTokenRequest);
        }
        return null;
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
            throw new UserNotFoundException();
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
    }
}
