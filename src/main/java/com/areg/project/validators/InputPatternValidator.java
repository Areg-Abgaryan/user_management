/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InputPatternValidator {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()-+=^.])(?=\\S+$).{8,20}$";
    private static final String NAME_PATTERN = "^[A-Z][a-z]+$";

    private static Pattern pattern;

    private static final String FIRST_NAME_RULES = "Invalid first name format! First name must start with an uppercase letter, followed by lowercase letters.";
    private static final String LAST_NAME_RULES = "Invalid last name format! Last name must start with an uppercase letter, followed by lowercase letters.";
    private static final String PASSWORD_RULES = "Invalid password format! Password must have 8-20 characters, including uppercase, lowercase, digits, and special characters";

    public void validatePassword(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        final Matcher matcher = pattern.matcher(password);

        if (! matcher.matches()) {
            throw new IllegalArgumentException(PASSWORD_RULES);
        }
    }

    public void validateFirstName(String firstName) {
        pattern = Pattern.compile(NAME_PATTERN);
        final Matcher matcher = pattern.matcher(firstName);
        if (! matcher.matches()) {
            throw new IllegalArgumentException(FIRST_NAME_RULES);
        }
    }

    public void validateLastName(String lastName) {
        pattern = Pattern.compile(NAME_PATTERN);
        final Matcher matcher = pattern.matcher(lastName);
        if (! matcher.matches()) {
            throw new IllegalArgumentException(LAST_NAME_RULES);
        }
    }
}
