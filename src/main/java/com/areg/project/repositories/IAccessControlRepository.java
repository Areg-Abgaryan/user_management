/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repositories;

import com.areg.project.models.entities.AccessControlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IAccessControlRepository extends JpaRepository<AccessControlEntity, Long> {

    Set<AccessControlEntity> findByUserGroupIdIn(Set<Long> userGroupIds);
}