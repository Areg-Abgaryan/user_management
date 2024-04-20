/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Getter;

import java.util.Set;

@Getter
@Entity(name = "user_group")
@Table(name = "user_group", schema = "public")
public class UserGroupEntity extends CreateUpdateEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "userGroups")
    private Set<UserEntity> users;
}