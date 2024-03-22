/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class CreateUpdateEntity extends BaseEntity {

    @Column(name = "uuid", unique = true)
    protected UUID uuid;

    @CreatedDate
    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "update_date")
    protected LocalDateTime updateDate;
}
