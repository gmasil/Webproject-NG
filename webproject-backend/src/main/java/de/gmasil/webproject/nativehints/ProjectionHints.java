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
