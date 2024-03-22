package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Table(name = "object_group")
public class ObjectGroupEntity extends BaseEntity {

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "objectGroup")
    private Set<ObjectEntity> objects;
}
