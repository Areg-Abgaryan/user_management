/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class AccessControlNotFoundException extends RuntimeException {

    public AccessControlNotFoundException(Long userGroupId) {
        super("Access control for user group id " + userGroupId + " not found");
    }
}
