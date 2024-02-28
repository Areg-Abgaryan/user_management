/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

public class WrongOtpException extends RuntimeException {
    public WrongOtpException() {
        super("Wrong one time password provided");
    }
}
