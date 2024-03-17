/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' not found");
    }
}
