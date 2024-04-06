/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

    private Utils() { }

    //  Method to get epoch seconds of the moment
    public static long getEpochSecondsNow() {
        return System.currentTimeMillis() / 1000;
    }

    // Method to convert epoch seconds to LocalDateTime
    public static LocalDateTime epochSecondsToLocalDateTime(long epochSeconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZoneId.systemDefault());
    }
}