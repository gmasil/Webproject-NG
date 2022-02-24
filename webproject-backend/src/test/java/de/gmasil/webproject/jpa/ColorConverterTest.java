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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ColorConverterTest {

    private ColorConverter converter = new ColorConverter();

    private static Map<String, Color> samples = new HashMap<>();

    @BeforeAll
    static void setupSamples() {
        samples.put("#ff0000", new Color(255, 0, 0));
        samples.put("#00ff00", new Color(0, 255, 0));
        samples.put("#0000ff", new Color(0, 0, 255));
        samples.put("#6901a5", new Color(105, 1, 165));
        samples.put("#b63f21", new Color(182, 63, 33));
        samples.put("#1ab82c", new Color(26, 184, 44));
        samples.put("#d5ee24", new Color(213, 238, 36));
        samples.put("#4a2ce0", new Color(74, 44, 224));
        samples.put("#f2e41a", new Color(242, 228, 26));
        samples.put("#021a2b", new Color(2, 26, 43));
        samples.put("#eff564", new Color(239, 245, 100));
        samples.put("#4fda34", new Color(79, 218, 52));
        samples.put("#763f9a", new Color(118, 63, 154));
        samples.put("#d913c3", new Color(217, 19, 195));
        samples.put("#2d0765", new Color(45, 7, 101));
        samples.put("#4c800e", new Color(76, 128, 14));
        samples.put("#bd9051", new Color(189, 144, 81));
        samples.put("#7b9f82", new Color(123, 159, 130));
        samples.put("#7b1312", new Color(123, 19, 18));
        samples.put("#e5ce8b", new Color(229, 206, 139));
        samples.put("#38d1d1", new Color(56, 209, 209));
        samples.put("#d07afc", new Color(208, 122, 252));
        samples.put("#70ffdb", new Color(112, 255, 219));
    }

    @Test
    void testColorConversion() {
        for (Entry<String, Color> sample : samples.entrySet()) {
            String hex = sample.getKey();
            Color color = sample.getValue();
            assertColor(color, hex);
            assertHexColor(hex, color);
        }
    }

    private void assertColor(Color color, String hex) {
        assertThat(converter.convertToDatabaseColumn(color), is(equalTo(hex)));
    }

    private void assertHexColor(String hex, Color color) {
        assertThat(converter.convertToEntityAttribute(hex), is(equalTo(color)));
    }
}
