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
package de.gmasil.webproject.jpa.theme;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.user.User;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @RestResource(path = "/default")
    @Query("SELECT t FROM THEME t, PROPERTY p WHERE p.key = '" + Property.DEFAULT_THEME + "' AND t.id = p.value")
    public Theme findDefault();

    @RestResource(path = "/preset")
    public List<Theme> findByPresetTrue();

    @PreAuthorize("isAuthenticated()")
    @Query("SELECT t FROM THEME t, USER u WHERE t.creator = u AND u = ?#{principal}")
    public List<Theme> findAllByCreator();

    @RestResource(path = "/available")
    @Query("SELECT DISTINCT(t) FROM THEME t, USER u WHERE t.preset = true OR (t.creator = u AND u.id = ?#{principal instanceOf T(de.gmasil.webproject.jpa.user.User) ? principal.getId() : -1L})")
    public List<Theme> findAllByPresetTrueOrCreatorCurrentUser();

    @RestResource(exported = false)
    @Query("SELECT t FROM THEME t WHERE t.preset = true OR t.creator = :user")
    public List<Theme> findAllAvailable(@Param("user") User user);

    @RestResource(exported = false)
    @Query("SELECT t FROM THEME t WHERE t.id = :id AND (t.preset = true OR t.creator = :user)")
    public Optional<Theme> findAvailableById(@Param("id") Long id, @Param("user") User user);

    @RestResource(exported = false)
    @Query("SELECT t FROM THEME t WHERE t.id = :id AND t.creator = :user")
    public Optional<Theme> findByIdAndCreator(@Param("id") Long id, @Param("user") User user);
}
