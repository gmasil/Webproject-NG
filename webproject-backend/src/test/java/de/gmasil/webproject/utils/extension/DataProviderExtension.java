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
package de.gmasil.webproject.utils.extension;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;

public class DataProviderExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ProvideUser[] annotations = context.getRequiredTestMethod().getAnnotationsByType(ProvideUser.class);
        if (annotations.length > 0) {
            UserService userService = getAutowired(UserService.class, context);
            RoleRepository roleRepo = getAutowired(RoleRepository.class, context);
            for (ProvideUser annotation : annotations) {
                User user = userService.findByUsername(annotation.name()).orElseGet(() -> {
                    return userService.save(userService.encodePassword(
                            User.builder().username(annotation.name()).password(annotation.password()).build()));
                });
                if (annotation.admin() && !user.hasRole("ADMIN")) {
                    Role role = roleRepo.findByNameOrCreate("ADMIN");
                    user.addRole(role);
                    userService.save(user);
                }
            }
        }
    }

    private static <T> T getAutowired(Class<T> clazz, ExtensionContext context) {
        return SpringExtension.getApplicationContext(context).getBean(clazz);
    }
}
