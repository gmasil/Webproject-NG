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
package de.gmasil.webproject.utils.resttemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.web.client.RestTemplate;

import de.gmasil.webproject.dto.UserDto;

public class AdvRestTemplate extends RestTemplate {

    public void loginAdmin() {
        login("admin", "admin");
    }

    public void loginUser() {
        login("Simon", "pass1");
    }

    public void login(String username, String password) {
        postForObject("/performlogin?username=" + username + "&password=" + password, null, String.class);
        UserDto user = getForObject("/api/users/current", UserDto.class);
        String loadedName = user != null ? user.getUsername() : null;
        assertThat("Login failed", loadedName, is(equalTo(username)));
    }
}
