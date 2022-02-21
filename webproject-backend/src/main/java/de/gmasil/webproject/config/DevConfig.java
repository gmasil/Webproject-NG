/*
 * Webproject NG
 * Copyright © 2021 - 2022 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International
 * Public License ("Public License").
 *
 * Webproject NG is non-free software: you can redistribute
 * it and/or modify it under the terms of the Public License.
 *
 * You should have received a copy of the Public License along
 * with Webproject NG. If not, see
 * https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
 */
package de.gmasil.webproject.config;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Cannot simply use @Profile("dev") in spring native as the property is evaluated during build time
@Component
public class DevConfig implements WebMvcConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${spring.profiles.active:}")
    private String profiles;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (isDevMode()) {
            LOG.info("Running in DEV mode");
            registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
        }
    }

    private boolean isDevMode() {
        return Arrays.stream(profiles.split(",")).map(String::trim).map(String::toLowerCase)
                .anyMatch(profile -> Objects.equals(profile, "dev"));
    }
}
