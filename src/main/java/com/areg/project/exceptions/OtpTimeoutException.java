/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class OtpTimeoutException extends RuntimeException {
    public OtpTimeoutException() {
        super("One time password input timeout");
    }
}
