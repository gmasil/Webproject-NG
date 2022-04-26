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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.config.AppProperties;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@Story("Test app properties REST endpoint")
class AppPropertiesRestControllerTest extends GherkinTest {

    @Autowired
    private RestTemplateFactory factory;

    @Scenario("App properties can be read publicly")
    void testAppProperties(Reference<AppProperties> appProperties) {
        AdvRestTemplate rest = factory.createRestTemplate();
        when("an anonymous user retrieves the active theme", () -> {
            System.out.println(rest.getForObject("/api/app/config", String.class));
            appProperties.set(rest.getForObject("/api/app/config", AppProperties.class));
        });
        then("the default theme is returned", () -> {
            assertThat(appProperties.get().isPublicAccess(), is(equalTo(false)));
            assertThat(appProperties.get().getGit(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getBuild(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getBuild().getVersion(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getBuild().getTime(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getCommit(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getCommit().getId(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getCommit().getAbbrev(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getCommit().getDescribe(), is(notNullValue()));
            assertThat(appProperties.get().getGit().getCommit().getDescribeShort(), is(notNullValue()));
        });
    }
}
