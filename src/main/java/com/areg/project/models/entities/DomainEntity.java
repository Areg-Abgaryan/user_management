/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.Objects;
import java.util.Set;
import lombok.Getter;

@Getter
@Entity(name = "domain")
@Table(name = "domain")
public class DomainEntity extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @OneToMany(mappedBy = "domain")
    private Set<PermissionEntity> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (DomainEntity) o;
        return name.equals(that.name) && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}

