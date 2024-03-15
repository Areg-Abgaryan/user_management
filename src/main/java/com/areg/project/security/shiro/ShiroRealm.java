/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.shiro;

import com.areg.project.managers.EncryptionManager;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionManager encryptionManager;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final var upToken = (UsernamePasswordToken) token;
        final var password = new String((char[]) token.getCredentials());
        final UserEntity user = userService.getActiveUserByEmail(upToken.getUsername());
        if (user == null) {
            throw new UnknownAccountException("Invalid user");
        }

        final String encryptedPassword = encryptionManager.encrypt(password, user.getSalt());
        if (! encryptedPassword.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("Invalid password");
        }

        return new SimpleAuthenticationInfo(user.getEmail(), password, getName());
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectIdentifier, String... permissions) throws AuthorizationException {
        SecurityUtils.getSubject().isPermitted();
        super.checkPermissions(subjectIdentifier, permissions);
    }
}
