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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@Story("Error controller shows vue frontend on all endpoints")
class ErrorRedirectControllerTest extends GherkinTest {

    @Autowired
    private RestTemplateFactory factory;

    @Scenario("All non-api endpoints show vue frontend")
    void testAllEndpointsShowVueIndex() {
        testAllEndpointsShowVueIndex("/");
        testAllEndpointsShowVueIndex("/login");
        testAllEndpointsShowVueIndex("/something/not/existing");
    }

    private void testAllEndpointsShowVueIndex(String uri) {
        Reference<ResponseEntity<String>> response = new Reference<>();
        AdvRestTemplate rest = factory.createRestTemplate();
        when("the URI '" + uri + "' is called", () -> {
            response.set(rest.getForEntity(uri, String.class));
        });
        then("the vue frontend is shown", () -> {
            assertThat(response.get().getStatusCode(), is(equalTo(HttpStatus.OK)));
            assertThat(response.get().getBody(), containsString("We're sorry but"));
            assertThat(response.get().getBody(),
                    containsString("doesn't work properly without JavaScript enabled. Please enable it to continue"));
        });
    }
}
