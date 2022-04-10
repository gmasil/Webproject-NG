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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.web.client.RestTemplate;

import de.gmasil.webproject.dto.UserDto;

public class AdvRestTemplate extends RestTemplate {

    public void logout() {
        postForObject("/logout", null, String.class);
        UserDto user = getForObject("/api/users/current", UserDto.class);
        String loadedName = user != null ? user.getUsername() : null;
        assertThat("Logout failed", loadedName, is(nullValue()));
    }

    public void loginAdmin() {
        login("admin", "admin");
    }

    public void loginUser() {
        login("user", "user");
    }

    public void loginUser(String password) {
        login("user", password);
    }

    private void login(String username, String password) {
        postForObject("/performlogin?username=" + username + "&password=" + password, null, String.class);
        UserDto user = getForObject("/api/users/current", UserDto.class);
        String loadedName = user != null ? user.getUsername() : null;
        assertThat("Login failed", loadedName, is(equalTo(username)));
    }

    public ThemeRestTemplate themes() {
        return new ThemeRestTemplate(this);
    }
}
