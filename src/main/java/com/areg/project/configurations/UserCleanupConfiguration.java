/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.configurations;

import com.areg.project.models.enums.UserStatus;
import com.areg.project.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Run a background job for removing users with status 'UNVERIFIED' from the system
 */
@Configuration
@EnableScheduling
public class UserCleanupConfiguration {

    private final IUserRepository userRepository;

    @Autowired
    public UserCleanupConfiguration(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS,
            initialDelayString = "${user.cleanup.initial.delay}", fixedDelayString = "${user.cleanup.fixed.delay}")
    @Transactional
    public void removeUnverifiedUsers() {
        userRepository.deleteAllByStatus(UserStatus.UNVERIFIED);
    }
}
