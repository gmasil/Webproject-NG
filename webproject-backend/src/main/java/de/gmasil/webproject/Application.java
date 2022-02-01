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
package de.gmasil.webproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.newprojection.RoleProjectionNEW;
import de.gmasil.webproject.newprojection.ThemeProjectionNEW;
import de.gmasil.webproject.newprojection.UserProjectionNEW;
import de.gmasil.webproject.newprojection.VideoProjectionNEW;

@EnableJpaAuditing
@SpringBootApplication
@TypeHint(types = { //
        Role.class, //
        RoleProjectionNEW.class, //
        Theme.class, //
        ThemeProjectionNEW.class, //
        User.class, //
        UserProjectionNEW.class, //
        Video.class, //
        VideoProjectionNEW.class //
}, access = { //
        TypeAccess.PUBLIC_CONSTRUCTORS, //
        TypeAccess.PUBLIC_FIELDS, //
        TypeAccess.PUBLIC_METHODS //
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
