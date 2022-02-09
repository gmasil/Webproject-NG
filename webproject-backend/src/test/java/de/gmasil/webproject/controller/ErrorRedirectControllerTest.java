/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
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
