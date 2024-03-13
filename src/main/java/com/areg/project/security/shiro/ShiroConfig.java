/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.security.shiro;

import com.areg.project.controllers.EndpointsConstants;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
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

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        final var chainDefinition = new DefaultShiroFilterChainDefinition();

        //  These paths should be accessible without requiring authentication.
        //  FIXME !! Check this
        chainDefinition.addPathDefinition(EndpointsConstants.API, "anon");
        chainDefinition.addPathDefinition(EndpointsConstants.LOGIN, "anon");

        chainDefinition.addPathDefinition(EndpointsConstants.LOGOUT, "logout");
        return chainDefinition;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainDefinition chainDefinition) {
        final var shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(EndpointsConstants.LOGIN);
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());
        return shiroFilterFactoryBean;
    }
}