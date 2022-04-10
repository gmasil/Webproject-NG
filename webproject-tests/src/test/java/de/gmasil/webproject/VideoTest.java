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
package de.gmasil.webproject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.webproject.pages.LoginPage;
import de.gmasil.webproject.pages.VideosPage;
import de.gmasil.webproject.utils.ScreenshotDriver;
import de.gmasil.webproject.utils.SetupIntegrationTestContext;

@SetupIntegrationTestContext
class VideoTest {

    @Autowired
    private ScreenshotDriver screenshot;

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private VideosPage videosPage;

    @Test
    void testVideos() {
        loginPage.open();
        loginPage.performLogin("admin", "admin");
        videosPage.open();
        assertThat(videosPage.countVideos(), is(equalTo(10)));
        screenshot.waitUntilImagesareLoaded();
        screenshot.takeInTestFolder(1000);
    }
}
