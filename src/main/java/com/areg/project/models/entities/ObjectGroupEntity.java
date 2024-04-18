package com.areg.project.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity(name = "object_group")
@Table(name = "object_group", schema = "public")
public class ObjectGroupEntity extends CreateUpdateEntity {

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "objectGroup")
    private Set<ObjectEntity> objects;

    @ManyToMany(mappedBy = "objectGroups")
    private Set<AccessControlEntity> accessControls;
}
