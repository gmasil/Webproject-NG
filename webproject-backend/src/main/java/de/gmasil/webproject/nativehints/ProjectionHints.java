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
package de.gmasil.webproject.nativehints;

import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

import de.gmasil.webproject.dto.RoleDto;
import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.dto.UserDto;
import de.gmasil.webproject.dto.VideoDto;
import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.video.Video;

@Configuration
@TypeHint(types = { //
        Role.class, //
        RoleDto.class, //
        Theme.class, //
        ThemeDto.class, //
        User.class, //
        UserDto.class, //
        Video.class, //
        VideoDto.class //
}, access = { //
        TypeAccess.PUBLIC_CONSTRUCTORS, //
        TypeAccess.PUBLIC_FIELDS, //
        TypeAccess.PUBLIC_METHODS //
})
public class ProjectionHints {
}
