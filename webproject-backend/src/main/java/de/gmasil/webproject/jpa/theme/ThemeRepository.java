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

import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.projection.ThemeProjection;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("SELECT t FROM THEME t, PROPERTY p WHERE p.key = '" + Property.DEFAULT_THEME + "' AND t.id = p.value")
    public Optional<Theme> findDefault();

    @Query("SELECT t FROM THEME t, PROPERTY p WHERE p.key = '" + Property.DEFAULT_THEME + "' AND t.id = p.value")
    public Optional<ThemeProjection> findDefaultProjection();

    @Query("SELECT t FROM THEME t, USER u WHERE t = u.activeTheme AND u.id = :userId")
    public Optional<ThemeProjection> findProjectionActiveByUser(@Param("userId") Long userId);

    @Query("SELECT u.activeTheme FROM USER u WHERE u.id = :userId")
    public Optional<Theme> findActiveByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM THEME t WHERE t.id = :id AND t.creator = :user")
    public Optional<Theme> findByIdAndCreator(@Param("id") Long id, @Param("user") User user);

    @Query("SELECT t FROM THEME t WHERE t.id = :id AND (t.preset = true OR t.creator = :user)")
    public Optional<Theme> findAvailableById(@Param("id") Long id, @Param("user") User user);

    @Query("SELECT t FROM THEME t WHERE t.preset = true OR t.creator = :user")
    public List<Theme> findAllAvailable(@Param("user") User user);

    public List<ThemeProjection> findAllProjectionByPresetTrue();

    public List<ThemeProjection> findAllProjectionByPresetTrueOrCreator(User creator);
}
