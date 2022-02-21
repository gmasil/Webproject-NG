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
package de.gmasil.webproject.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.config.AppProperties;
import de.gmasil.webproject.controller.PermitAll;

@RestController
@RequestMapping("/api/app")
public class AppPropertiesRestController {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ModelMapper mapper;

    @PermitAll
    @GetMapping("/config")
    public AppProperties config() {
        AppProperties props = new AppProperties();
        mapper.map(appProperties, props);
        return props;
    }
}
