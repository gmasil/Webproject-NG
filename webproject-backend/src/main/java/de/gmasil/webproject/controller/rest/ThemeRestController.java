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
package de.gmasil.webproject.controller.rest;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.jpa.ColorConverter;
import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.service.UserProvider;

@RestController
@RequestMapping("/api/themes")
public class ThemeRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ColorConverter colorConverter;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<Object> postTheme(@RequestBody ThemeDto themeDto) {
        User user = userProvider.getCurrent();
        if (themeDto.isPreset()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Posting preset is not allowed");
        }
        if (themeDto.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Posting specific ID is not allowed");
        }
        user = entityManager.merge(user);
        Theme theme = mapper.map(themeDto, Theme.class);
        theme.setCreator(user);
        theme = themeRepo.save(theme);
        mapper.map(theme, themeDto);
        return ResponseEntity.ok(themeDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<String> putTheme(@PathVariable Long id, @RequestBody ThemeDto themeDto) {
        User user = userProvider.getCurrent();
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

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/resetactive")
    public ResponseEntity<String> resetActiveTheme() {
        User user = userProvider.getCurrent();
        user = entityManager.merge(user);
        user.setActiveTheme(null);
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @PutMapping("/setdefault/{id}")
    public ResponseEntity<String> setDefaultTheme(@PathVariable Long id) {
        Optional<Theme> theme = themeRepo.findPresetById(id);
        if (theme.isPresent()) {
            propertyRepo.setProperty(Property.DEFAULT_THEME, "" + id);
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
            themes = themeRepo.findAllProjectionByPresetTrueOrCreator(user);
        } else {
            themes = themeRepo.findAllProjectionByPresetTrue();
        }
        return ResponseEntity.ok(themes);
    }

    @PermitAll
    @GetMapping("/active")
    public ResponseEntity<Object> activeTheme() {
        User user = userProvider.getCurrent();
        Optional<ThemeDto> themeDto;
        if (user != null) {
            themeDto = themeRepo.findProjectionActiveByUser(user.getId());
            if (!themeDto.isPresent()) {
                themeDto = themeRepo.findDefaultProjection();
            }
        } else {
            themeDto = themeRepo.findDefaultProjection();
        }
        if (themeDto.isPresent()) {
            return ResponseEntity.ok(themeDto.get());
        }
        LOG.warn("There is no default theme set");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PermitAll
    @GetMapping(value = "/active.css", produces = "text/css")
    public String activeThemeCss() {
        User user = userProvider.getCurrent();
        Theme theme = null;
        if (user != null) {
            theme = themeRepo.findActiveByUser(user.getId()).orElseGet(() -> null);
        }
        if (theme == null) {
            theme = themeRepo.findDefault().orElseThrow(() -> new IllegalStateException("No default theme found"));
        }
        return theme.toCss(colorConverter);
    }
}
