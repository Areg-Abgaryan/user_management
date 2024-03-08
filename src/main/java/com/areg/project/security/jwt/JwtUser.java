/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.jwt;

import com.areg.project.models.UserStatus;
import com.areg.project.models.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Setter;

import java.util.Collection;

@Setter
public class JwtUser implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    //  We use email as the username in this project
    @Override
    public String getUsername() { return email; }

    @JsonIgnore
    @Override
    public String getPassword() { return password; }

    @Override
    public boolean isEnabled() { return enabled; }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    public static JwtUser create(UserEntity user) {
        final var jwtUser = new JwtUser();
        jwtUser.setId(user.getId());
        jwtUser.setEmail(user.getEmail());
        jwtUser.setPassword(user.getPassword());
        jwtUser.setFirstName(user.getFirstName());
        jwtUser.setLastName(user.getLastName());
        jwtUser.setEnabled(user.getStatus().equals(UserStatus.ACTIVE));
        return jwtUser;
    }
}