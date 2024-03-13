/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.managers.RoleManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.areg.project.controllers.EndpointsConstants.ROLE;

@RestController("role-controller")
@RequestMapping(ROLE)
@Tag(name = "Role Controller")
public class RoleController {

    private final RoleManager roleManager;

    @Autowired
    public RoleController(RoleManager roleManager) {
        this.roleManager = roleManager;
    }


    @Operation(summary = "Get all roles", description = "Get all roles in the system")
    @GetMapping(EndpointsConstants.GET_ALL)
    public ResponseEntity<?> getAllRoles() {
        try {
            return ResponseEntity.ok(roleManager.getAllRoles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
