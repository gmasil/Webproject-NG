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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import de.gmasil.webproject.jpa.ColorConverter;

@Service
@ConfigurationPropertiesBinding
public class StringToColorConverter extends JsonDeserializer<Color> implements Converter<String, Color> {

    @Autowired
    private ColorConverter converter;

    @Override
    public Color convert(String source) {
        return converter.convertToEntityAttribute(source);
    }

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return converter.convertToEntityAttribute(p.getText());
    }
}
