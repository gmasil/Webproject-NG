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
package de.gmasil.webproject.utils.resttemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extensions.EnableTestDataImportBeforeAll;
import de.gmasil.webproject.utils.serialization.PaginatedResponse;

@SetupTestContext
@EnableTestDataImportBeforeAll
class RestTemplateSessionInterceptorTest {

    private static final ParameterizedTypeReference<PaginatedResponse<Video>> PAGINATED_VIDEO = new ParameterizedTypeReference<PaginatedResponse<Video>>() {
    };

    @Autowired
    private RestTemplateFactory restTemplateFactory;

    @Test
    void testPrivateVideosAreShown() {
        AdvRestTemplate rest = restTemplateFactory.createRestTemplate();
        rest.loginUser();

        ResponseEntity<PaginatedResponse<Video>> exchange = rest.exchange("/api/videos", HttpMethod.GET, null,
                PAGINATED_VIDEO);
        PaginatedResponse<Video> pagedVideos = exchange.getBody();
        assertThat(pagedVideos.getContent(), hasSize(greaterThan(0)));
    }
}
