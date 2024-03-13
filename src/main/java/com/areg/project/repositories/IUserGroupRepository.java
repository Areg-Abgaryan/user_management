/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.repositories;

import com.areg.project.models.entities.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserGroupRepository extends JpaRepository<UserGroupEntity, Long> {
}
