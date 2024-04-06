/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException(String message) {
        super("Session expired ! " + message);
    }
}
