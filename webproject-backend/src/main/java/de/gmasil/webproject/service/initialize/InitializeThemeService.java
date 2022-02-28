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

import java.lang.invoke.MethodHandles;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;

@Service
public class InitializeThemeService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    private InitializeProperties properties;

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    public void initTheme() {
        if (themeRepo.count() == 0) {
            Theme theme = new Theme();
            mapper.map(properties.getTheme(), theme);
            theme.setPreset(true);
            theme = themeRepo.save(theme);
            propertyRepo.setProperty(Property.DEFAULT_THEME, "" + theme.getId());
            LOG.info("Initialized theme");
        }
    }
}
