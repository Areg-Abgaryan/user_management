/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.shiro;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager() {
        final var securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(createRealm());
        return securityManager;
    }

    @Bean
    public ShiroRealm createRealm() {
        return new ShiroRealm();
    }
}