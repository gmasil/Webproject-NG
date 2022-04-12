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
package de.gmasil.webproject.service.initialize;

import java.lang.invoke.MethodHandles;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;
import de.gmasil.webproject.service.initialize.InitializeProperties.AdminProperties;

@Service
public class InitializeAdminService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String INITIAL_ADMIN_USER_MSG = "Initialized admin user\n\n" //
            + "#####################################\n" //
            + "Initial admin user\n" //
            + "Username: {}\n" //
            + "Password: {}\n" //
            + "#####################################\n";

    @Autowired
    private InitializeProperties properties;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepo;

    @Transactional
    public void initAdminUser() {
        if (!userService.hasUsers()) {
            AdminProperties admin = properties.getAdmin();
            User user = User.builder().username(admin.getName()).password(admin.getPassword()).build();
            Role role = roleRepo.findByNameOrCreate("ADMIN");
            user.addRole(role);
            userService.encodePassword(user);
            userService.save(user);
            LOG.info(INITIAL_ADMIN_USER_MSG, admin.getName(), admin.getPassword());
        }
    }
}
