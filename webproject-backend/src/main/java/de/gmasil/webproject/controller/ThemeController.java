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
package de.gmasil.webproject.controller;

import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.jpa.ColorConverter;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.service.UserProvider;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    @Autowired
    private LocalProxy proxy;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private ColorConverter colorConverter;

    @GetMapping("/active")
    public ResponseEntity<String> activeTheme(@PathVariable(required = false) Optional<String> param,
            @RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request)
            throws URISyntaxException {

        User user = userProvider.getCurrent();
        String path = "/api/themes/search/default";
        if (user != null) {
            path = "/api/users/" + user.getId() + "/activeTheme";
        }
        return proxy.proxy(path, body, method, request);
    }

    @GetMapping(value = "/active.css", produces = "text/css")
    public String activeThemeCss() {
        Theme theme;
        User user = userProvider.getCurrent();
        if (user == null) {
            theme = themeRepo.findDefault();
        } else {
            theme = user.getActiveTheme();
        }
        return theme.toCss(colorConverter);
    }
}
