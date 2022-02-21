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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
class ErrorRedirectControllerTest {

    @Autowired
    private RestTemplate rest;

    @Test
    void testAllEndpointsShowVueIndex() {
        checkUri("/");
        checkUri("/login");
        checkUri("/something/not/existing");
    }

    private void checkUri(String uri) {
        ResponseEntity<String> responseEntity = rest.getForEntity(uri, String.class);
        checkResponseEntity(responseEntity);
    }

    private void checkResponseEntity(ResponseEntity<String> responseEntity) {
        assertThat(responseEntity.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(responseEntity.getBody(), containsString("We're sorry but"));
        assertThat(responseEntity.getBody(),
                containsString("doesn't work properly without JavaScript enabled. Please enable it to continue"));
    }
}
