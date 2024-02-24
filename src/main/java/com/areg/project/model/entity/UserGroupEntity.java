/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "t_user_group")
public class UserGroupEntity extends BaseEntity {

    @Column(name = "external_id")
    private UUID externalId;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "userGroup")
    private Set<UserEntity> users;
}
