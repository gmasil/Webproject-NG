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
package de.gmasil.webproject.dto;

import java.util.Set;
import java.util.stream.Collectors;

import de.gmasil.webproject.jpa.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private Set<RoleDto> roles;
    private ThemeDto activeTheme;

    /**
     * Do not use! Workaround for tests in spring native
     *
     * @param s must be the String "null"
     */
    public UserDto(String s) {
        if (!"null".equals(s)) {
            throw new IllegalArgumentException(
                    "Constructor UserDto(String) may only be used as a workaround for tests in spring native");
        }
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles().stream().map(RoleDto::new).collect(Collectors.toSet());
        if (user.getActiveTheme() != null) {
            this.activeTheme = new ThemeDto(user.getActiveTheme());
        }
    }
}
