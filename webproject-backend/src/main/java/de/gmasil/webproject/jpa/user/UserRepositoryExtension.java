/*
 * Webproject NG
 * Copyright © 2022 Gmasil
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
package de.gmasil.webproject.jpa.user;

import java.util.List;
import java.util.Optional;

import de.gmasil.webproject.projection.UserProjection;

public interface UserRepositoryExtension {

    public List<User> findAllWithRole(String role);

    public Optional<User> findWithRolesByUsername(String username);

    public Optional<User> findWithRolesById(Long id);

    public Optional<UserProjection> findProjectionById(Long id);
}
