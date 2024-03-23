/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class CreateUpdateEntity extends BaseEntity {

    @Column(name = "uuid", unique = true, nullable = false)
    protected UUID uuid;

    //  Epoch seconds of creation date
    @Column(name = "created_at", nullable = false)
    protected long createdAt;

    //  Epoch seconds of update date
    @Column(name = "updated_at", nullable = false)
    protected long updatedAt;
}
