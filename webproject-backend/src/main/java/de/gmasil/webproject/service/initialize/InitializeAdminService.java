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
package de.gmasil.webproject.service.initialize;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;
import de.gmasil.webproject.service.initialize.InitializeProperties.AdminProperties;

@Service
public class InitializeAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String INITIAL_ADMIN_USER_MSG = "\n\n" //
            + "#####################################\n" //
            + "Initial admin user\n" //
            + "Username: {}\n" //
            + "Password: {}\n" //
            + "#####################################\n";
    @Autowired
    private InitializeProperties properties;

    @Autowired
    private UserService userService;

    public void initAdminUser() {
        if (!userService.hasUsers()) {
            AdminProperties admin = properties.getAdmin();
            User user = User.builder().username(admin.getName()).password(admin.getPassword()).build();
            userService.encodePassword(user);
            userService.save(user);
            LOG.info("Initialized admin user" + INITIAL_ADMIN_USER_MSG, admin.getName(), admin.getPassword());
        }
    }
}
