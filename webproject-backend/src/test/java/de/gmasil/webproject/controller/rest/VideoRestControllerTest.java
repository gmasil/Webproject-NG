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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extension.EnableTestDataImport;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;
import de.gmasil.webproject.utils.serialization.PaginatedResponse;

@SetupTestContext
@EnableTestDataImport
@Story("Test video REST endpoint")
class VideoRestControllerTest extends GherkinTest {

    private static final ParameterizedTypeReference<PaginatedResponse<Video>> PAGINATED_VIDEO = new ParameterizedTypeReference<>() {
    };

    @Autowired
    private RestTemplateFactory restTemplateFactory;

    @Scenario("Videos are shown to authenticated users")
    void testVideosAreShown(Reference<PaginatedResponse<Video>> response) {
        AdvRestTemplate rest = restTemplateFactory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user executes a GET request on /api/videos", () -> {
            response.set(rest.exchange("/api/videos", HttpMethod.GET, null, PAGINATED_VIDEO).getBody());
        });
        then("some videos are shown", () -> {
            assertThat(response.get().getContent(), hasSize(greaterThan(0)));
        });
    }

    @Scenario("Video pages are shown to authenticated users")
    void testVideoPageSize(Reference<PaginatedResponse<Video>> response) {
        AdvRestTemplate rest = restTemplateFactory.createRestTemplate();
        given("a user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user executes a GET request on /api/videos?size=1", () -> {
            response.set(rest.exchange("/api/videos?size=1", HttpMethod.GET, null, PAGINATED_VIDEO).getBody());
        });
        then("exactly one video is shown", () -> {
            assertThat(response.get().getContent(), hasSize(1));
        });
    }

    @Scenario("No videos are shown to unauthenticated users")
    void testNoVideosAreShownUnauthenticated(Reference<Integer> statusCode) {
        AdvRestTemplate rest = restTemplateFactory.createRestTemplate();
        given("the user is not authenticated", () -> {
        });
        when("the user executes a GET request on /api/videos", () -> {
            try {
                rest.exchange("/api/videos", HttpMethod.GET, null, PAGINATED_VIDEO).getBody();
            } catch (HttpClientErrorException e) {
                statusCode.set(e.getRawStatusCode());
            }
        });
        then("status code 401 Unauthorized is shown", () -> {
            assertThat(statusCode.get(), is(equalTo(401)));
        });
    }
}
