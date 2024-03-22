/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //  FIXME !! remove http after enabling https
                .allowedOrigins("http://localhost", "https://localhost")
                .allowedMethods()
                .allowedMethods(getAllRequestMethodNames())
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }


    private static String[] getAllRequestMethodNames() {
        return Arrays.stream(RequestMethod.values()).map(Enum::name).toArray(String[]::new);
    }
}