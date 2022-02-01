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
package de.gmasil.webproject.service.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.newprojection.ThemeProjectionNEW;

@Service
public class ThemeProjectionConverter implements Converter<Theme, ThemeProjectionNEW> {

    @Override
    public ThemeProjectionNEW convert(Theme theme) {
        return new ThemeProjectionNEW(theme);
    }
}