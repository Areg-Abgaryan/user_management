/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InputPatternValidator {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Pattern pattern;
}
