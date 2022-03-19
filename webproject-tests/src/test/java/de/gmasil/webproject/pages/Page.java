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
package de.gmasil.webproject.pages;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.webproject.utils.UrlBuilder;

public abstract class Page {

    @Autowired
    WebDriver browser;

    @Autowired
    UrlBuilder url;

    public void open() {
        browser.navigate().to(url.from(getUrl()));
        browser.findElement(getPageLoadChecker());
        assertThat(browser.findElement(getPageLoadChecker()), is(notNullValue()));
    }

    public abstract String getUrl();

    public abstract By getPageLoadChecker();
}
