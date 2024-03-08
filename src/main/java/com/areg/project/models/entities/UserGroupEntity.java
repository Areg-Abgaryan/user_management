/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "user_group", schema = "public")
public class UserGroupEntity extends BaseEntity {

    @Column(name = "external_id", unique = true)
    private UUID externalId;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "userGroup")
    private Set<UserEntity> users;
}