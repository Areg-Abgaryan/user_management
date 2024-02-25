/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * User Management Application
 *
 * @since 2024
 * @version 1.0
 * @author Areg Abgaryan
 */

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.areg.project.repositories")
@EntityScan(basePackages = "com.areg.project.models.entities")
public class UserManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
        System.out.println("Some code");
    }
}