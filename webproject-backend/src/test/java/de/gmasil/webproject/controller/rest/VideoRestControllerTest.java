/*
 * Webproject NG
 * Copyright © 2022 Gmasil
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
package de.gmasil.webproject.controller.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.utils.EnablePublicAccess;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extensions.EnableTestDataImportBeforeAll;
import de.gmasil.webproject.utils.serialization.PaginatedResponse;

@SetupTestContext
@EnablePublicAccess
@EnableTestDataImportBeforeAll
class VideoRestControllerTest {

    private static final ParameterizedTypeReference<PaginatedResponse<Video>> PAGINATED_VIDEO = new ParameterizedTypeReference<PaginatedResponse<Video>>() {
    };

    @Autowired
    private RestTemplate rest;

    @Test
    void testVideosAreShown() {
        ResponseEntity<PaginatedResponse<Video>> exchange = rest.exchange("/api/videos", HttpMethod.GET, null,
                PAGINATED_VIDEO);
        PaginatedResponse<Video> pagedVideos = exchange.getBody();
        assertThat(pagedVideos.getContent(), hasSize(greaterThan(0)));
    }

    @Test
    void testVideoPageSize() {
        ResponseEntity<PaginatedResponse<Video>> exchange = rest.exchange("/api/videos?size=1", HttpMethod.GET, null,
                PAGINATED_VIDEO);
        PaginatedResponse<Video> pagedVideos = exchange.getBody();
        assertThat(pagedVideos.getContent(), hasSize(1));
    }
}
