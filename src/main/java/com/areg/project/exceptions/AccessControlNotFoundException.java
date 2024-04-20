/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.exceptions;

import java.util.Set;
import java.util.StringJoiner;

public class AccessControlNotFoundException extends RuntimeException {

    public AccessControlNotFoundException(Set<Long> userGroupIds) {
        super("Access control for user groups '" + convert(userGroupIds)  + "' was not found");
    }


    private static String convert(Set<Long> userGroupIds) {
        final var joiner = new StringJoiner(", ");
        for (Long id : userGroupIds) {
            joiner.add(id.toString());
        }
        return "[" + joiner + "]";
    }
}
