/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import com.areg.project.models.entities.UserEntity;
import com.areg.project.services.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    //  FIXME !! Check this, this implements spring security
    @Autowired
    public JwtUserDetailsService(@Lazy UserService userService) {
        this.userService = userService;
    }

    //  We find here by email, not by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity user = userService.getUserByEmail(username);
        return JwtUser.create(user);
    }
}
