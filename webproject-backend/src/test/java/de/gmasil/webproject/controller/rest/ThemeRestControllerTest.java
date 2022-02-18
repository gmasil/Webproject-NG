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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extensions.EnableTestDataImportBeforeEach;
import de.gmasil.webproject.utils.rest.AdvRestTemplate;
import de.gmasil.webproject.utils.rest.RestTemplateFactory;

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
