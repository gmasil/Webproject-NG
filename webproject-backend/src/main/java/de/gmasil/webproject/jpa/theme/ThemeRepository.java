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
package de.gmasil.webproject.jpa.theme;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.gmasil.webproject.dto.ThemeDto;
import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.user.User;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("SELECT t FROM THEME t, PROPERTY p WHERE p.key = '" + Property.DEFAULT_THEME + "' AND t.id = p.value")
    public Optional<Theme> findDefault();

    @Query("SELECT u.activeTheme FROM USER u WHERE u.id = :userId")
    public Optional<Theme> findActiveByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM THEME t WHERE t.id = :id AND t.creator = :user")
    public Optional<Theme> findByIdAndCreator(@Param("id") Long id, @Param("user") User user);

    @Query("SELECT t FROM THEME t WHERE t.id = :id AND (t.preset = true OR t.creator = :user)")
    public Optional<Theme> findAvailableById(@Param("id") Long id, @Param("user") User user);

    @Query("SELECT t FROM THEME t WHERE t.id = :id AND t.preset = true")
    public Optional<Theme> findPresetById(@Param("id") Long id);

    @Query("SELECT t FROM THEME t WHERE t.preset = true OR t.creator = :user")
    public List<Theme> findAllAvailable(@Param("user") User user);

    public List<ThemeDto> findAllProjectionByPresetTrue();

    public List<ThemeDto> findAllProjectionByPresetTrueOrCreator(User creator);

    public List<Theme> findAllByName(String name);
}
