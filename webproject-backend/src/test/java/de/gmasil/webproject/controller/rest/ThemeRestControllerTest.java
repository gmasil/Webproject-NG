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
package de.gmasil.webproject.controller.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extensions.EnableTestDataImportBeforeEach;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@EnableTestDataImportBeforeEach
class ThemeRestControllerTest {

    @Autowired
    private RestTemplateFactory factory;

    @Test
    void testGetDefaultActiveTheme() {
        AdvRestTemplate template = factory.createRestTemplate();
        ResponseEntity<ThemeDto> response = template.getForEntity("/api/themes/active", ThemeDto.class);
        assertThat(response.getBody().getName(), is(equalTo("Webproject Purple")));
    }
}
