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
package de.gmasil.webproject.utils.resttemplate;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import de.gmasil.webproject.dto.ThemeDto;

public class ThemeRestTemplate {

    private RestTemplate rest;

    public ThemeRestTemplate(RestTemplate rest) {
        this.rest = rest;
    }

    public List<ThemeDto> getAvailable() {
        return getList("/api/themes/available");
    }

    public ThemeDto active() {
        return get("/api/themes/active");
    }

    public ThemeDto get(String url) {
        return rest.getForObject(url, ThemeDto.class);
    }

    public List<ThemeDto> getList(String url) {
        return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ThemeDto>>() {
        }).getBody();
    }

    public ThemeDto post(ThemeDto theme) {
        return rest.postForObject("/api/themes", theme, ThemeDto.class);
    }

    public void put(ThemeDto theme) {
        rest.put("/api/themes/" + theme.getId(), theme);
    }

    public void activate(ThemeDto theme) {
        activate(theme.getId());
    }

    public void activate(Long id) {
        rest.put("/api/themes/activate/" + id, null);
    }
}
