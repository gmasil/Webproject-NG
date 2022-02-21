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

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
import de.gmasil.webproject.dto.UserDto;
import de.gmasil.webproject.dto.UserPasswordDto;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.user.UserService;
import de.gmasil.webproject.service.UserProvider;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @PermitAll
    @GetMapping("/current")
    public ResponseEntity<Object> currentUser() {
        User user = userProvider.getCurrent();
        if (user != null) {
            Optional<UserDto> eagerUser = userRepo.findProjectionById(user.getId());
            if (eagerUser.isPresent()) {
                return ResponseEntity.ok(eagerUser.get());
            }
        }
        return ResponseEntity.ok("null");
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UserPasswordDto userPassword) {
        User user = userProvider.getCurrent();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        user = entityManager.merge(user);
        if (!userService.verifyPassword(userPassword.getCurrentPassword(), user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password does not match");
        }
        user.setPassword(userPassword.getNewPassword());
        userService.encodePassword(user);
        userService.save(user);
        return ResponseEntity.ok().build();
    }
}
