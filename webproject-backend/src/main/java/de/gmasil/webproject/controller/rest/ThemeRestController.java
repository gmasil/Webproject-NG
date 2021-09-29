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

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.jpa.ColorConverter;
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
    private ModelMapper mapper;

    @Autowired
    private ColorConverter colorConverter;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/activate/{id}")
    public ResponseEntity<String> setActiveTheme(@PathVariable Long id) {
        User user = userProvider.getCurrent();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
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

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTheme(@PathVariable Long id, @RequestBody ThemeDto themeDto) {
        User user = userProvider.getCurrent();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        Optional<Theme> theme = themeRepo.findByIdAndCreator(id, user);
        if (theme.isPresent()) {
            mapper.map(themeDto, theme.get());
            theme.get().setPreset(false);
            themeRepo.save(theme.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Theme with id " + id + " not found");
        }
    }

    @PermitAll
    @GetMapping("/available")
    public ResponseEntity<Object> available() {
        User user = userProvider.getCurrent();
        List<ThemeDto> themes;
        if (user != null) {
            themes = themeRepo.findAllDtoByPresetTrueOrCreator(user);
        } else {
            themes = themeRepo.findAllDtoByPresetTrue();
        }
        return ResponseEntity.ok(themes);
    }

    @PermitAll
    @GetMapping("/active")
    public ResponseEntity<Object> activeTheme() {
        User user = userProvider.getCurrent();
        Optional<ThemeDto> themeDto;
        if (user != null) {
            themeDto = themeRepo.findDtoActiveByUser(user.getId());
        } else {
            themeDto = themeRepo.findDefaultDto();
        }
        if (themeDto.isPresent()) {
            return ResponseEntity.ok(themeDto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PermitAll
    @GetMapping(value = "/active.css", produces = "text/css")
    public String activeThemeCss() {
        User user = userProvider.getCurrent();
        Theme theme;
        if (user != null) {
            theme = user.getActiveTheme();
        } else {
            theme = themeRepo.findDefault().orElseThrow(() -> new IllegalStateException("No default theme found"));
        }
        return theme.toCss(colorConverter);
    }
}
