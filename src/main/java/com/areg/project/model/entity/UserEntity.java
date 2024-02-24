/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.model.entity;

import com.areg.project.model.UserStatus;
import org.springframework.data.annotation.CreatedDate;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "t_user")
public class UserEntity extends BaseEntity {

    @Column(name = "external_id", unique = true)
    private UUID externalId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "salt")
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @CreatedDate
    @Column(name = "last_login")
    private LocalDateTime lastLoginTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroupEntity userGroup;
}
