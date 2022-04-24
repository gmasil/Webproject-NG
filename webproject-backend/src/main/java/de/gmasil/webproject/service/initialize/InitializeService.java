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
package de.gmasil.webproject.service.initialize;

import java.lang.invoke.MethodHandles;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class InitializeService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String TIMEZONE_HIBERNATE_PROPERTY = "spring.jpa.properties.hibernate.jdbc.time_zone";
    public static final String TIMEZONE = "UTC";

    @Value("${" + TIMEZONE_HIBERNATE_PROPERTY + "}")
    private String timezoneProperty;

    @Autowired
    private InitializeProperties properties;

    @Autowired
    private InitializeAdminService adminService;

    @Autowired
    private InitializeThemeService themeService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void initialize() {
        initTimezone();
        if (!properties.isSkip()) {
            StopWatch watch = new StopWatch();
            watch.start();
            adminService.initAdminUser();
            themeService.initTheme();
            watch.stop();
            LOG.info("Initialized application in {}s", watch.getTotalTimeSeconds());
        }
        eventPublisher.publishEvent(new InitializeFinishedEvent(this));
    }

    private void initTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(TIMEZONE));
        if (!TIMEZONE.equals(TimeZone.getDefault().getID())) {
            LOG.warn("Incorrect timezone detected: {}, but should always be {}", TimeZone.getDefault().getID(),
                    TIMEZONE);
        }
        if (!TIMEZONE.equals(timezoneProperty)) {
            LOG.warn("Incorrect hibernate timezone detected: {}, but should always be {}", timezoneProperty, TIMEZONE);
        }
    }
}
