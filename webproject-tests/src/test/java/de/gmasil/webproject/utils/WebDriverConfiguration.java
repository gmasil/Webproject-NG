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
package de.gmasil.webproject.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.ProtocolHandshake;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class WebDriverConfiguration {

    @Value("${webdriver.gecko.driver:''}")
    private String geckoDriver;

    @Value("${webdriver.firefox.logfile:target/selenium-firefox.log}")
    private String firefoxLogfile;

    @Value("${webdriver.chrome.driver:''}")
    private String chromeDriver;

    @Value("${webdriver.browser:firefox}")
    private String browser;

    @Bean
    @Scope("singleton")
    public WebDriver webdriver() {
        WebDriver driver;
        ((Logger) LoggerFactory.getLogger(ProtocolHandshake.class)).setLevel(Level.WARN);
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", chromeDriver);
            driver = new ChromeDriver();
        } else {
            System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, geckoDriver);
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, firefoxLogfile);
            driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }
}
