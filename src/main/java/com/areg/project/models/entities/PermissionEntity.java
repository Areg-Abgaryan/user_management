/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Table(name = "permission")
public class PermissionEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private DomainEntity domain;

    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionEntity that = (PermissionEntity) o;
        return name.equals(that.name) && domain.equals(that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain);
    }
}
