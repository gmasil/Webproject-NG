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
package de.gmasil.webproject.config;

import java.awt.Color;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.gmasil.webproject.service.converter.ColorToStringConverter;
import de.gmasil.webproject.service.converter.StringToColorConverter;

@Configuration
public class JacksonConfig {

    @Bean
    public Module module(ColorToStringConverter colorToStringConverter, StringToColorConverter stringToColorConverter) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(colorToStringConverter);
        module.addDeserializer(Color.class, stringToColorConverter);
        return module;
    }
}
