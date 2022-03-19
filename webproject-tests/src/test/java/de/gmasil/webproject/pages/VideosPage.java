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

import org.openqa.selenium.By;

public class VideosPage extends Page {

    @Override
    public String getUrl() {
        return "/videos";
    }

    @Override
    public By getPageLoadChecker() {
        return By.cssSelector(".grid");
    }

    public int countVideos() {
        return browser.findElements(By.cssSelector(".grid > div")).size();
    }
}
