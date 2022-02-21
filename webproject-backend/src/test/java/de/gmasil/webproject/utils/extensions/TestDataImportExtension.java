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
package de.gmasil.webproject.utils.extensions;

import java.lang.invoke.MethodHandles;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.gmasil.webproject.service.dataimport.DataImportService;

public class TestDataImportExtension implements BeforeEachCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        LOG.info("Importing test data before \"{}\"", context.getDisplayName());
        DataImportService service = SpringExtension.getApplicationContext(context).getBean(DataImportService.class);
        service.performClean();
        service.performDataImport();
    }
}
