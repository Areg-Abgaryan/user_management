/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@EntityScan(basePackages = "com.areg.project")
@OpenAPIDefinition(
        info = @Info(title = "User Management Project", version = "1.0.0",
        contact = @Contact(name = "Areg Abgaryan", email = "abgaryan.areg@gmail.com", url = "https://www.linkedin.com/in/abgaryan-areg/")))
public class UserManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
        System.out.println("Some code");
    }
}