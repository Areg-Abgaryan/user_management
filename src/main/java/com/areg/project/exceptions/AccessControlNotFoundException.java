/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

import java.util.UUID;

public class AccessControlNotFoundException extends RuntimeException {

    public AccessControlNotFoundException(UUID userGroupUuid) {
        super("Access control for user group id '" + userGroupUuid + "' was not found");
    }
}
