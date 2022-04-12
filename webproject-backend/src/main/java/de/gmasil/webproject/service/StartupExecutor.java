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
package de.gmasil.webproject.service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.service.dataimport.DataImportProperties;
import de.gmasil.webproject.service.dataimport.DataImportService;
import de.gmasil.webproject.service.initialize.InitializeService;

@Service
public class StartupExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Autowired
    private DataImportProperties properties;

    @Autowired
    private InitializeService initializeService;

    @EventListener(ApplicationReadyEvent.class)
    public void executeStartup() throws IOException {
        dataImportService.cleanIfRequested();
        initializeService.initialize();
        dataImportService.importDataIfRequested();
        if (properties.isEnabled() && properties.isImportOnly()) {
            LOG.info("Dataimport only, shutting down...");
            ctx.close();
        }
    }
}
