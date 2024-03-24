/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import com.areg.project.models.enums.UserStatus;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "user")
@Table(name = "user", schema = "public")
public class UserEntity extends CreateUpdateEntity {

    @Column(name = "email", unique = true, updatable = false, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    //  Epoch seconds of last login date
    @CreatedDate
    @Column(name = "last_login_at")
    private long lastLoginAt;

    @Column(name = "otp")
    private int otp;

    @Column(name = "otp_creation_time")
    private long otpCreationTime;

    //  FIXME !! Change to Lazy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id")
    private UserGroupEntity userGroup;
}