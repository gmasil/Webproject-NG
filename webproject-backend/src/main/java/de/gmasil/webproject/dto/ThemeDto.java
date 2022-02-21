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
package de.gmasil.webproject.dto;

import java.awt.Color;

import org.modelmapper.ModelMapper;

import de.gmasil.webproject.jpa.theme.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {

    private Long id;
    private String name;
    private Color backgroundColor;
    private Color backgroundHighlightColor;
    private Color primaryColor;
    private Color secondaryColor;
    private Color textColor;
    private boolean preset;

    public ThemeDto(Theme theme) {
        new ModelMapper().map(theme, this);
    }
}
