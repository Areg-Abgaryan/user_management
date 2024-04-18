/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.converters;

import com.areg.project.models.dtos.UserGroupDTO;
import com.areg.project.models.entities.UserGroupEntity;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.testng.Assert.*;

public class UserGroupConverterTest {

    private UserGroupConverter userGroupConverter;

    @BeforeClass
    public void setUp() {
        userGroupConverter = new UserGroupConverter();
    }

    @Test(priority = 1)
    public void testFromEntityToDto_NullCollection() {
        //  When input collection is null, expect null return value
        final Set<UserGroupDTO> dtoSet = userGroupConverter.fromEntityToDto(null);
        assertNull(dtoSet);
    }

    @Test(priority = 2)
    public void testFromEntityToDto_EmptyCollection() {
        //  Given an empty collection for input, expect an empty set after conversion
        final Collection<UserGroupEntity> entities = new HashSet<>();
        final Set<UserGroupDTO> dtoSet = userGroupConverter.fromEntityToDto(entities);
        assertNotNull(dtoSet);
        assertTrue(dtoSet.isEmpty());
    }

    @Test(priority = 3)
    public void testFromEntityToDto_NonEmptyCollection() {
        //  Build input user groups
        final Collection<UserGroupEntity> entities = new HashSet<>();
        entities.add(buildUserGroup(1));
        entities.add(buildUserGroup(2));
        entities.add(buildUserGroup(3));

        //  Convert user groups
        final Set<UserGroupDTO> dtoSet = userGroupConverter.fromEntityToDto(entities);
        assertNotNull(dtoSet);
        assertEquals(dtoSet.size(), 3);

        //  Expect the DTO set to contain 3 DTOs, each with non-null UUID and name fields
        for (UserGroupDTO dto : dtoSet) {
            assertTrue(StringUtils.isNotBlank(dto.getName()));
            assertTrue(StringUtils.isNotBlank(dto.getUuid().toString()));
        }
    }


    private static UserGroupEntity buildUserGroup(int number) {
        final var entity = new UserGroupEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setName("User Group " + number);
        return entity;
    }
}
