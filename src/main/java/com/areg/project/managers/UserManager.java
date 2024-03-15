/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.converters.UserConverter;
import com.areg.project.exceptions.ForbiddenOperationException;
import com.areg.project.exceptions.UserNotFoundException;
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
import com.areg.project.validators.UserDataValidator;
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

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserInputValidator userInputValidator;
    private final UserDataValidator userDataValidator;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserManager(UserService userService, UserConverter userConverter,
                       UserInputValidator userInputValidator, UserDataValidator userDataValidator,
                       EncryptionManager encryptionManager, EmailVerificationManager emailVerificationManager,
                       JwtProvider jwtProvider) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.userInputValidator = userInputValidator;
        this.userDataValidator = userDataValidator;
        this.encryptionManager = encryptionManager;
        this.emailVerificationManager = emailVerificationManager;
        this.jwtProvider = jwtProvider;
    }


    public UserSignupResponse createUnverifiedUser(UserSignUpDTO signUpDto) throws AddressException {

        userInputValidator.validateUserInput(signUpDto);

        //  Check whether the user already has tried to sign up
        UserEntity entity = userService.getUserByEmail(signUpDto.getEmail());

        if (entity == null) {
            //  Convert from dto, set crypto fields, create a new user in the system and send otp email
            entity = userConverter.fromSignUpDtoToEntity(signUpDto);
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

    public UserSignupResponse verifyUserEmail(UserVerifyEmailDTO verifyEmailDto) throws AddressException {

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

    public UserLoginResponse login(UserLoginDTO loginDto) throws AddressException {

        userInputValidator.validateUserInput(loginDto);

        final String email = loginDto.getEmail();

        //  FIXME !! I don't think this is a good way. Have 2 implementations of UserService, for active users and for all ones ?

        //  Check whether the user exists in the system
        final UserSignupResponse userResponse = getUserByEmail(email);
        if (userResponse == null) {
            throw new UserNotFoundException(email);
        }

        userDataValidator.blockInactiveUserLogin(userResponse.getStatus(), email);

        //  Log in and update last log in time
        final var token = new UsernamePasswordToken(email, loginDto.getPassword());
        SecurityUtils.getSubject().login(token);

        //  Update user's last log in time
        updateLastLoginTime(email, Utils.getCurrentDateAndTime());

        // Generate JWT token with user permissions wildcard
        final JwtToken jwtToken = jwtProvider.createJwtToken(userResponse.getId(), email);

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