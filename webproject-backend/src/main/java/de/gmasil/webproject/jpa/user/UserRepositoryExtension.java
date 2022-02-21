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
package de.gmasil.webproject.jpa.user;

import java.util.List;
import java.util.Optional;

import de.gmasil.webproject.dto.UserDto;

public interface UserRepositoryExtension {

    public List<User> findAllWithRole(String role);

    public Optional<User> findWithRolesByUsername(String username);

    public Optional<User> findWithRolesById(Long id);

    public Optional<UserDto> findProjectionById(Long id);
}
