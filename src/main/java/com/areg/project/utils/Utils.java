/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class Utils {

    private Utils() { }

    //  Method to get current date and time
    public static LocalDateTime getCurrentDateAndTime() {
        final var currentDate = new Date();
        final Instant instant = currentDate.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    //  Method to get epoch seconds of the moment
    public static long getEpochSecondsNow() {
        return System.currentTimeMillis() / 1000;
    }

    // Method to convert LocalDateTime to epoch seconds
    public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZoneOffset zoneOffset = zoneId.getRules().getOffset(localDateTime);
        return localDateTime.toInstant(zoneOffset).getEpochSecond();
    }

    // Method to convert epoch seconds to LocalDateTime
    public static LocalDateTime epochSecondsToLocalDateTime(long epochSeconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
    }
}