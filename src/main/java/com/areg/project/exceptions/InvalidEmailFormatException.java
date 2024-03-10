/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class InvalidEmailFormatException extends RuntimeException {

    public InvalidEmailFormatException(String email) {
        super(email + " email format is invalid");
    }
}
