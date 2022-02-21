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
package de.gmasil.webproject.service.converter;

import java.awt.Color;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.gmasil.webproject.jpa.ColorConverter;

@Service
@ConfigurationPropertiesBinding
public class ColorToStringConverter extends JsonSerializer<Color> implements Converter<Color, String> {

    @Autowired
    private ColorConverter converter;

    @Override
    public String convert(Color source) {
        return converter.convertToDatabaseColumn(source);
    }

    @Override
    public void serialize(Color source, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(converter.convertToDatabaseColumn(source));
    }

    @Override
    public Class<Color> handledType() {
        return Color.class;
    }
}
