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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extension.DisableTestDataImport;
import de.gmasil.webproject.utils.extension.EnableDataProvider;
import de.gmasil.webproject.utils.extension.EnableTestDataImport;
import de.gmasil.webproject.utils.extension.ProvideUser;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@EnableDataProvider
@EnableTestDataImport
@Story("Test theme REST endpoint")
class ThemeRestControllerTest extends GherkinTest {

    @Autowired
    private RestTemplateFactory factory;

    @Autowired
    private ThemeRepository themeRepo;

    // ************************* POST *************************

    @ProvideUser
    @DisableTestDataImport
    @Scenario("A theme can be saved")
    void testThemesCanBeSaved(Reference<ResponseEntity<ThemeDto>> response) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user saves a theme", () -> {
            ThemeDto theme = createTestThemeDto();
            response.set(rest.postForEntity("/api/themes", theme, ThemeDto.class));
        });
        then("the theme is saved", () -> {
            assertThat(response.get().getBody().getName(), is(equalTo("ThemePersistenceTest")));
        });
    }

    @ProvideUser
    @DisableTestDataImport
    @Scenario("A theme cannot be saved as preset")
    void testThemeCannotBeSavedAsPreset(Reference<HttpClientErrorException> exception) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user saves a preset theme", () -> {
            ThemeDto theme = createTestThemeDto();
            theme.setPreset(true);
            try {
                rest.postForEntity("/api/themes", theme, ThemeDto.class);
            } catch (HttpClientErrorException e) {
                exception.set(e);
            }
        });
        then("the request is rejected", () -> {
            assertThat(exception.get().getMessage(), containsString("Posting preset is not allowed"));
        });
    }

    @ProvideUser
    @DisableTestDataImport
    @Scenario("A theme with explicit id cannot be saved")
    void testThemeCannotBeSavedWithId(Reference<HttpClientErrorException> exception) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user saves a theme with id", () -> {
            ThemeDto theme = createTestThemeDto();
            theme.setId(13L);
            try {
                rest.postForEntity("/api/themes", theme, ThemeDto.class);
            } catch (HttpClientErrorException e) {
                exception.set(e);
            }
        });
        then("the request is rejected", () -> {
            assertThat(exception.get().getMessage(), containsString("Posting specific ID is not allowed"));
        });
    }

    // ************************* PUT *************************

    @ProvideUser
    @DisableTestDataImport
    @Scenario("A theme can be updated")
    void testThemesCanBeUpdated(Reference<ThemeDto> savedTheme) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        and("a theme exists", () -> {
            ThemeDto theme = createTestThemeDto();
            savedTheme.set(rest.postForEntity("/api/themes", theme, ThemeDto.class).getBody());
            assertThat(savedTheme.get(), is(notNullValue()));
            assertThat(savedTheme.get().getId(), is(notNullValue()));
        });
        when("the user updates the theme", () -> {
            savedTheme.get().setName("SomeStrangeNewName");
            rest.put("/api/themes/" + savedTheme.get().getId(), savedTheme.get());
        });
        then("the theme is saved", () -> {
            ThemeDto theme = rest.getForEntity("/api/themes/" + savedTheme.get().getId(), ThemeDto.class).getBody();
            assertThat(theme.getName(), is(equalTo("SomeStrangeNewName")));
        });
    }

    @ProvideUser
    @DisableTestDataImport
    @Scenario("A non-existent theme can not be updated")
    void testNonExistentThemeCanNotBeUpdated(Reference<HttpClientErrorException> exception) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        and("no theme exists", () -> {
        });
        when("the user updates the theme with id 815", () -> {
            ThemeDto theme = createTestThemeDto();
            try {
                rest.put("/api/themes/815", theme);
            } catch (HttpClientErrorException e) {
                exception.set(e);
            }
        });
        then("the request is rejected", () -> {
            assertThat(exception.get().getMessage(), containsString("Theme with id 815 not found"));
        });
    }

    // ************************* Active Theme *************************

    @Scenario("The default theme is shown as active theme")
    void testGetDefaultActiveTheme(Reference<ResponseEntity<ThemeDto>> response) {
        AdvRestTemplate rest = factory.createRestTemplate();
        when("an anonymous user retrieves the active theme", () -> {
            response.set(rest.getForEntity("/api/themes/active", ThemeDto.class));
        });
        then("the default theme is returned", () -> {
            assertThat(response.get().getBody().getName(), is(equalTo("Webproject Purple")));
        });
    }

    @Scenario("A users default theme changes with the system default theme")
    void testDefaultActiveThemeChangesWithSystem() {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user has the default theme selected", () -> {
            rest.loginUser();
            rest.put("/api/themes/resetactive", null);
            ResponseEntity<ThemeDto> response = rest.getForEntity("/api/themes/active", ThemeDto.class);
            assertThat(response.getBody().getName(), is(equalTo("Webproject Purple")));
        });
        when("the admin changes the default theme", () -> {
            rest.loginAdmin();
            Long newThemeId = themeRepo.findAllByName("Webproject Dark").get(0).getId();
            rest.put("/api/themes/setdefault/" + newThemeId, null);
        });
        then("the active theme of the user was also changed", () -> {
            rest.loginUser();
            ResponseEntity<ThemeDto> response = rest.getForEntity("/api/themes/active", ThemeDto.class);
            assertThat(response.getBody().getName(), is(equalTo("Webproject Dark")));
        });
    }

    @Scenario("The active theme can be retrieved as CSS")
    void testGetActiveThemeAsCss(Reference<String> css) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("a user has the default theme selected", () -> {
            rest.loginUser();
            rest.put("/api/themes/resetactive", null);
            ResponseEntity<ThemeDto> response = rest.getForEntity("/api/themes/active", ThemeDto.class);
            assertThat(response.getBody().getName(), is(equalTo("Webproject Purple")));
        });
        when("the default theme is requested as CSS", () -> {
            css.set(rest.getForEntity("/api/themes/active.css", String.class).getBody());
        });
        then("the CSS file is correct", () -> {
            assertThat(css.get(), is(equalTo(
                    ":root{--theme-background:#5f0066;--theme-background-highlight:#300033;--theme-primary:#f221ff;--theme-secondary:#b253b8;--theme-text:#f2f2f2}")));
        });
    }

    // ************************* Utils *************************

    private ThemeDto createTestThemeDto() {
        return new ThemeDto(Theme.builder() //
                .name("ThemePersistenceTest") //
                .backgroundColor(Color.GRAY) //
                .backgroundHighlightColor(Color.LIGHT_GRAY) //
                .primaryColor(Color.RED) //
                .secondaryColor(Color.ORANGE) //
                .textColor(Color.white) //
                .preset(false) //
                .build());
    }
}
