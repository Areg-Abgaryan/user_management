package com.areg.project.converters;

import com.areg.project.models.dtos.RoleDTO;
import com.areg.project.models.entities.RoleEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.testng.Assert.*;

public class RoleConverterTest {

    private RoleConverter roleConverter;

    @BeforeClass
    public void setUp() {
        roleConverter = new RoleConverter();
    }

    @Test(priority = 1)
    public void testFromEntityToDto_NullCollection() {
        // Test conversion from null collection to DTO set
        final Set<RoleDTO> dtoSet = roleConverter.fromEntityToDto(null);
        assertNull(dtoSet);
    }

    @Test(priority = 2)
    public void testFromEntityToDto_EmptyCollection() {
        // Test conversion from empty collection to DTO set
        final Collection<RoleEntity> entities = Collections.emptySet();
        final Set<RoleDTO> dtoSet = roleConverter.fromEntityToDto(entities);
        assertNotNull(dtoSet);
        assertTrue(dtoSet.isEmpty());
    }

    @Test(priority = 3)
    public void testFromEntityToDto_NonEmptyCollection() {
        // Test conversion from non-empty collection to DTO set
        final var entity = new RoleEntity();
        final String testRole = "Admin";
        entity.setName(testRole);

        //  Convert roles
        final Collection<RoleEntity> entities = Collections.singletonList(entity);
        Set<RoleDTO> dtoSet = roleConverter.fromEntityToDto(entities);
        assertNotNull(dtoSet);
        assertFalse(dtoSet.isEmpty());

        // Get the first DTO from the set
        final RoleDTO dto = dtoSet.iterator().next();
        assertNotNull(dto);
        assertEquals(testRole, dto.getName());
    }
}