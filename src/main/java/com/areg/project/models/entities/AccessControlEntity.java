/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity(name = "access_control")
@Table(name = "access_control")
public class AccessControlEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_group_id")
    private UserGroupEntity userGroup;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "access_control_id"),
            inverseJoinColumns = @JoinColumn(name = "object_group_id"))
    private Set<ObjectGroupEntity> objectGroups;
}
