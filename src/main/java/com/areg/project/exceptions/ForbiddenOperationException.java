/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException() {
        super();
    }

    public ForbiddenOperationException(String message) {
        super(message);
    }
}