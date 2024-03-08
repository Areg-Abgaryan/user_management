/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.controllers.EndpointsConstants;
import com.areg.project.converters.UserConverter;
import com.areg.project.exceptions.OtpTimeoutException;
import com.areg.project.exceptions.WrongOtpException;
import com.areg.project.models.dtos.requests.user.UserSignUpDTO;
import com.areg.project.models.dtos.requests.user.UserVerifyEmailDTO;
import com.areg.project.models.dtos.responses.user.UserSignupResponse;
import com.areg.project.models.entities.AccessControlEntity;
import com.areg.project.models.entities.DomainEntity;
import com.areg.project.models.entities.ObjectEntity;
import com.areg.project.models.entities.ObjectGroupEntity;
import com.areg.project.models.entities.PermissionEntity;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.models.entities.UserGroupEntity;
import com.areg.project.services.implementations.AccessControlService;
import com.areg.project.services.implementations.UserService;
import com.areg.project.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserManager {

    private final UserService userService;
    private final AccessControlService accessControlService;
    private final UserConverter userConverter;
    private final EncryptionManager encryptionManager;
    private final EmailVerificationManager emailVerificationManager;

    @Autowired
    public UserManager(UserService userService, AccessControlService accessControlService, UserConverter userConverter,
                       EncryptionManager encryptionManager, EmailVerificationManager emailVerificationManager) {
        this.userService = userService;
        this.accessControlService = accessControlService;
        this.userConverter = userConverter;
        this.encryptionManager = encryptionManager;
        this.emailVerificationManager = emailVerificationManager;
    }

    public UserSignupResponse createUnverifiedUser(UserSignUpDTO signUpDto) throws InvalidObjectException {

        if (! isValidUser(signUpDto)) {
            throw new InvalidObjectException("Data cannot be empty");
        }

        final UserEntity entity = userConverter.fromSignUpDtoToEntity(signUpDto);

        //  Set salt
        final String salt = encryptionManager.generateSalt();
        entity.setSalt(salt);

        //  Set encrypted password
        final String encryptedPassword = encryptionManager.encrypt(signUpDto.getPassword(), salt);
        entity.setPassword(encryptedPassword);

        //  Create one time password and set to the user entity
        final int otp = emailVerificationManager.generateOneTimePassword();
        final long otpCreationTime = Utils.getEpochSecondsNow();
        entity.setOtp(otp);
        entity.setOtpCreationTime(otpCreationTime);
        //  FIXME !! Validate when the user with that email already exists
        //  FIXME !! Check whether i can save both abgaryan.areg@gmail.com && abgaryan.areg@broadcom.com
        //  Save unverified user
        final UserEntity savedEntity = userService.createUnverifiedUser(entity);

        //  Convert to response type
        final UserSignupResponse savedResponse = userConverter.fromEntityToSignUpResponse(savedEntity);

        //  Set otp verification instructions
        savedResponse.setOtpVerificationInstructions(createOTPInstructionsMessage(savedResponse.getEmail()));

        //  Send one time password to the destination email
        emailVerificationManager.sendEmail(signUpDto.getEmail(), otp);

        return savedResponse;
    }

    public UserSignupResponse verifyUserEmail(UserVerifyEmailDTO verifyEmailDto) throws InvalidObjectException {

        if (! isValidUser(verifyEmailDto)) {
            throw new InvalidObjectException("Data cannot be empty");
        }

        //  Get epoch seconds of the moment
        final long now = Utils.getEpochSecondsNow();

        //  Get the user with specified email
        final UserEntity entity = userService.findUserByEmail(verifyEmailDto.getEmail());

        //  Check whether the specified password is correct
        if (! entity.getPassword().equals(encryptionManager.encrypt(verifyEmailDto.getPassword(), entity.getSalt()))) {
            throw new AuthenticationException();
        } else {

            //  Check otp creation time. Timeout if 120 seconds passed
            if (entity.getOtpCreationTime() + 120 < now) {
                userService.updateWithNoOtpData(entity);
                throw new OtpTimeoutException();
            }

            //  Check otp
            if (verifyEmailDto.getOtp() != entity.getOtp()) {
                userService.updateWithNoOtpData(entity);
                throw new WrongOtpException();
            }

            //  Save the updated user
            final UserEntity savedEntity = userService.saveVerifiedUser(entity);

            //  Convert to response type and return
            return userConverter.fromEntityToSignUpResponse(savedEntity);
        }
    }

    public UserSignupResponse findUserById(UUID id) {
        final UserEntity userEntity = userService.findUserById(id);
        return userConverter.fromEntityToSignUpResponse(userEntity);
    }

    //  FIXME !! Return only ACTIVE users to api-s
    public UserSignupResponse findUserByEmail(String email) {
        final UserEntity userEntity = userService.findUserByEmail(email);
        return userConverter.fromEntityToSignUpResponse(userEntity);
    }

    public void updateLastLoginTime(String email, LocalDateTime loginDate) {
        userService.updateLastLoginTime(email, loginDate);
    }

    public Set<String> createUserPermissionsWildcards(UUID userId) {

        final UserEntity userById = userService.findUserById(userId);
        final UserGroupEntity userGroup = userById.getUserGroup();
        if (userGroup == null) {
            return Collections.emptySet();
        }

        final AccessControlEntity accessControl = accessControlService.getByUserGroupId(userGroup.getId());
        final Set<ObjectGroupEntity> objectGroupSet = accessControl.getObjectGroups();
        final Set<String> wildcards = new HashSet<>();

        for (var objectGroup : objectGroupSet) {
            if (! objectGroup.getObjects().isEmpty()) {
                final DomainEntity currentDomain = objectGroup.getObjects().stream()
                        .iterator()
                        .next()
                        .getDomain();
                wildcards.add(createUserPermissionsWildcard(
                        currentDomain, objectGroup.getObjects(), accessControl.getRole().getPermissions()));
            }
        }
        return wildcards;
    }


    private String createUserPermissionsWildcard(DomainEntity domain, Set<ObjectEntity> objects,
                                                 Set<PermissionEntity> permissions) {
        final String permissionsString;
        final String objectsString;

        List<PermissionEntity> result = new ArrayList<>();
        if (permissions != null && !permissions.isEmpty()) {
            result = permissions.stream()
                    .filter(perm -> perm.getDomain().equals(domain) && domain.getPermissions().contains(perm)).toList();
        }

        if (result.isEmpty()) {
            return "";
        }

        permissionsString = result.stream().map(permission -> permission.getName() + ",").collect(Collectors.joining());
        objectsString = objects.stream().map(objectEntity -> objectEntity.getExternalId().toString() + ",").collect(Collectors.joining());

        return domain.getCode() + ":" + StringUtils.chop(permissionsString) + ":" + StringUtils.chop(objectsString);
    }

    //  Check whether the user is valid or not
    private static boolean isValidUser(UserSignUpDTO userSignUpDto) {
        return StringUtils.isNoneBlank(userSignUpDto.getEmail(), userSignUpDto.getPassword(),
                userSignUpDto.getFirstName(), userSignUpDto.getLastName());
    }
    private static boolean isValidUser(UserVerifyEmailDTO userVerifyEmailDto) {
        return StringUtils.isNoneBlank(userVerifyEmailDto.getEmail(), userVerifyEmailDto.getPassword());
    }

    //  Create a message for the user with OTP instructions for verifying email during sign up
    private static String createOTPInstructionsMessage(String email) {
        return "A one time password is sent to your " + email
                + " address. Please, send it via this path " + EndpointsConstants.USER_SIGNUP_VERIFY_EMAIL;
    }
}