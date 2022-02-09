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
