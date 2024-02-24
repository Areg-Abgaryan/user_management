/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.logging;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


//  FIXME !! Test this logging system
//  This logger gets business logs as input and divides by user emails and session ids to multiple files
public class UserSessionLogger {

    private static final LoggerContext context = (LoggerContext) LogManager.getContext();
    private static String fileName;

    private static final Map<String, Logger> userEmailSessionToLogger = new HashMap<>();

    private static final Cache<String, Logger> userEmailSessionLoggerCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(1))
            .maximumSize(100)
            .build();

    private static final String ROLLING_FILE = "RollingFile";
    private static final String POLICIES = "Policies";
    private static final String SIZE_BASED_TRIGGERING_POLICY = "SizeBasedTriggeringPolicy";
    private static final String DEFAULT_ROLLOVER_STRATEGY = "DefaultRolloverStrategy";
    private static final String PATTERN_LAYOUT = "PatternLayout";

    //  Attributes
    private static final String FILE_NAME = "fileName";
    private static final String FILE_PATTERN = "filePattern";
    private static final String SIZE = "size";
    private static final String MAX = "max";
    private static final String PATTERN = "pattern";
    private static final String PATTERN_VALUE = "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n";
    private static final String ZIP_FILE_ENDING = "-%d{MM-dd-yy-HH-mm-ss}-%i.log.gz";


    public static void setFileName(String email, String sessionId) {
        fileName = email + "/" + sessionId;
    }

    public static Logger getLogger(String email, String sessionId) {
        final String emailSessionId = buildEmailSessionKey(email, sessionId);
        final var loggerFromCache = getLoggerFromCache(emailSessionId);
        return loggerFromCache != null ? loggerFromCache : getOrCreateLogger(email, sessionId);
    }

    public static String _msg(String sessionId, String email, String message) {
        return String.format("Session id : %s, Email : %s, Message : %s", sessionId, email, message);
    }


    //  Get specific logger for user and session id from the map
    private static Logger getOrCreateLogger(String email, String sessionId) {
        final String emailToSessionId = buildEmailSessionKey(email, sessionId);

        if (userEmailSessionToLogger.containsKey(emailToSessionId)) {
            return userEmailSessionToLogger.get(emailToSessionId);
        }
        else {
            //  Create a new logger, configure it and put in the map
            final Logger logger = createAndConfigureLogger(email, sessionId);
            userEmailSessionToLogger.put(emailToSessionId, logger);
            userEmailSessionLoggerCache.put(emailToSessionId, logger);
            return logger;
        }
    }

    //  Try to get logger from logging cache
    private static Logger getLoggerFromCache(String emailSessionId) {
        return userEmailSessionLoggerCache.getIfPresent(emailSessionId);
    }

    private static Logger createAndConfigureLogger(String email, String sessionId) {
        setFileName(email, sessionId);

        final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        final LayoutComponentBuilder layoutBuilder = builder.newLayout(PATTERN_LAYOUT)
                .addAttribute(PATTERN, PATTERN_VALUE);

        final AppenderComponentBuilder appenderBuilder = builder.newAppender(ROLLING_FILE, ROLLING_FILE)
                .addAttribute(FILE_NAME, String.format("src/main/resources/output/logs/%s.log", fileName))
                .addAttribute(FILE_PATTERN, "src/main/resources/output/logs/" + fileName + ZIP_FILE_ENDING)
                .add(layoutBuilder)
                .addComponent(builder.newComponent(POLICIES)
                        .addComponent(builder.newComponent(SIZE_BASED_TRIGGERING_POLICY).addAttribute(SIZE, "10 MB")))
                .addComponent(builder.newComponent(DEFAULT_ROLLOVER_STRATEGY).addAttribute(MAX, "5"));

        builder.add(appenderBuilder);

        // Configure the Logger with the RollingFileAppender
        final RootLoggerComponentBuilder customLogger = builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef(ROLLING_FILE));

        builder.add(customLogger);

        // Set the configuration and update the context
        context.start(builder.build());
        context.getRootLogger().getAppenders();

        final var logger = context.getLogger("");
        final String userSession = buildEmailSessionKey(email, sessionId);
        userEmailSessionToLogger.put(userSession, logger);
        userEmailSessionLoggerCache.put(userSession, logger);

        return logger;
    }

    private static String buildEmailSessionKey(String email, String sessionId) {
        return email + ":" + sessionId;
    }
}