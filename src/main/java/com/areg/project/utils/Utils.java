/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    private Utils() { }

    public static LocalDateTime getCurrentDateAndTime() {
        final var currentDate = new Date();
        final Instant instant = currentDate.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static long getEpochSecondsNow() {
        return System.currentTimeMillis() / 1000;
    }
}