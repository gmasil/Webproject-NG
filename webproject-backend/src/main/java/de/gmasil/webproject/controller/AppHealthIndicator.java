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
package de.gmasil.webproject.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.service.dataimport.CleanFinishedEvent;
import de.gmasil.webproject.service.dataimport.DataImportFinishedEvent;
import de.gmasil.webproject.service.initialize.InitializeFinishedEvent;

@Service
@RestController
public class AppHealthIndicator implements HealthIndicator {

    private Health health = Health.status("STARTING").build();

    @Override
    @PermitAll
    @GetMapping("/health")
    public Health health() {
        return health;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        health = Health.status("CLEANING").build();
    }

    @EventListener(CleanFinishedEvent.class)
    public void cleanFinished() {
        health = Health.status("INITIALIZING").build();
    }

    @EventListener(InitializeFinishedEvent.class)
    public void initializeFinished() {
        health = Health.status("IMPORTING").build();
    }

    @EventListener(DataImportFinishedEvent.class)
    public void dataImportFinished() {
        health = Health.up().build();
    }
}
