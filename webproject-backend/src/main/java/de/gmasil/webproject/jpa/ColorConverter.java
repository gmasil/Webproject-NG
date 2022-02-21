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
package de.gmasil.webproject.jpa;

import java.awt.Color;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Service;

@Service
@Converter
public class ColorConverter implements AttributeConverter<Color, String> {

    @Override
    public String convertToDatabaseColumn(Color color) {
        if (color == null) {
            return "#000000";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(0xFFFFFF & color.getRGB()));
        while (sb.length() < 6) {
            sb.insert(0, "0");
        }
        sb.insert(0, "#");
        return sb.toString();
    }

    @Override
    public Color convertToEntityAttribute(String string) {
        return Color.decode(string);
    }
}
