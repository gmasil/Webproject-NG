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

import java.awt.Color;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "init")
public class InitializeProperties {

    private boolean skip;
    private AdminProperties admin;
    private ThemeProperties theme;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AdminProperties {

        @NotEmpty
        private String name;

        @NotEmpty
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ThemeProperties {

        @NotEmpty
        private String name;

        @NotEmpty
        private Color backgroundColor;

        @NotEmpty
        private Color backgroundHighlightColor;

        @NotEmpty
        private Color primaryColor;

        @NotEmpty
        private Color secondaryColor;

        @NotEmpty
        private Color textColor;
    }
}
