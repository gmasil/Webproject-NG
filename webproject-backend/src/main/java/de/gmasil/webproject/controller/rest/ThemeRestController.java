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
package de.gmasil.webproject.controller.rest;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.service.UserProvider;

@RestController
@RequestMapping("/api/themes")
public class ThemeRestController {

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @PutMapping("/activate/{id}")
    public ResponseEntity<String> setActiveTheme(@PathVariable Long id) {
        User user = userProvider.getCurrent();
        user = entityManager.merge(user);
        Optional<Theme> theme = themeRepo.findAvailableById(id, user);
        if (theme.isPresent()) {
            user.setActiveTheme(theme.get());
            themeRepo.save(theme.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Theme with id " + id + " not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTheme(@PathVariable Long id, @RequestBody Theme theme) {
        if (theme.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Theme id and path variable do not match");
        }
        User user = userProvider.getCurrent();
        Optional<Theme> dbTheme = themeRepo.findByIdAndCreator(id, user);
        if (dbTheme.isPresent()) {
            dbTheme.get().map(theme);
            themeRepo.save(dbTheme.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Theme with id " + id + " not found");
        }
    }
}
