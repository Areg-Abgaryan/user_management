/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.controllers;

import com.areg.project.managers.UserGroupManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.areg.project.controllers.EndpointsConstants.USER_GROUP;

@RestController("user-group-controller")
@RequestMapping(USER_GROUP)
@Tag(name = "User Group Controller")
public class UserGroupController {

    private final UserGroupManager userGroupManager;

    @Autowired
    public UserGroupController(UserGroupManager userGroupManager) {
        this.userGroupManager = userGroupManager;
    }


    @Operation(summary = "Get all user groups", description = "Get all user groups in the system")
    @GetMapping(EndpointsConstants.GET_ALL)
    public ResponseEntity<?> getAllUserGroups() {
        try {
            return ResponseEntity.ok(userGroupManager.getAllUserGroups());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
