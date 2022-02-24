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

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extension.EnableTestDataImport;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@EnableTestDataImport
class ThemeRestControllerTest extends GherkinTest {

    @Autowired
    private RestTemplateFactory factory;

    @Autowired
    private ThemeRepository themeRepo;

    @Test
    void testGetDefaultActiveTheme() {
        AdvRestTemplate template = factory.createRestTemplate();
        ResponseEntity<ThemeDto> response = template.getForEntity("/api/themes/active", ThemeDto.class);
        assertThat(response.getBody().getName(), is(equalTo("Webproject Purple")));
    }

    @Scenario("A users default theme changes with the system default theme")
    void testDefaultActiveThemeChangesWithSystem() {
        AdvRestTemplate template = factory.createRestTemplate();
        given("a user has the default theme selected", () -> {
            template.loginUser();
            template.put("/api/themes/resetactive", null);
            ResponseEntity<ThemeDto> response = template.getForEntity("/api/themes/active", ThemeDto.class);
            assertThat(response.getBody().getName(), is(equalTo("Webproject Purple")));
        });
        when("the admin changes the default theme", () -> {
            template.loginAdmin();
            Long newThemeId = themeRepo.findAllByName("Webproject Dark").get(0).getId();
            template.put("/api/themes/setdefault/" + newThemeId, null);
        });
        then("the active theme of the user was also changed", () -> {
            template.loginUser();
            ResponseEntity<ThemeDto> response = template.getForEntity("/api/themes/active", ThemeDto.class);
            assertThat(response.getBody().getName(), is(equalTo("Webproject Dark")));
        });
    }
}
