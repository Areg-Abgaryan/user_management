/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    private Utils() { }

    public static String getSessionId() {
        final Subject currentUser = SecurityUtils.getSubject();
        final Session session = currentUser.getSession();
        return session.getId() != null ? session.getId().toString() : null;
    }

    public static LocalDateTime getCurrentDateAndTime() {
        final var currentDate = new Date();
        final Instant instant = currentDate.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}