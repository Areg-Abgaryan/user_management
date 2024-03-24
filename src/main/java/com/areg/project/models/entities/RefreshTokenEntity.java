/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "refresh_token")
@Table(name = "refresh_token", schema = "public")
public class RefreshTokenEntity extends CreateUpdateEntity {

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "salt", unique = true, nullable = false)
    private String salt;

    @Column(name = "expiring_at", nullable = false)
    private long expiringAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, updatable = false)
    private UserEntity userEntity;
}


