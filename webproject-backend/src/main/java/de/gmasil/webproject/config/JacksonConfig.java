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
