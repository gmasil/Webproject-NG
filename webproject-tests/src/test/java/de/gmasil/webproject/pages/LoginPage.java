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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.openqa.selenium.By;

public class LoginPage extends Page {

    private By inputUsername = By.id("login-input-username");
    private By inputPassword = By.id("login-input-password");
    private By inputSubmit = By.id("login-input-submit");
    private By spanUsername = By.id("navbar-username");

    @Override
    public String getUrl() {
        return "/login";
    }

    @Override
    public By getPageLoadChecker() {
        return inputUsername;
    }

    public void enterUsername(String username) {
        browser.findElement(inputUsername).sendKeys(username);
    }

    public void enterPassword(String password) {
        browser.findElement(inputPassword).sendKeys(password);
    }

    public void clickLogin() {
        browser.findElement(inputSubmit).click();
    }

    public void performLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        browser.findElement(inputSubmit).click();
        assertThat(browser.findElement(spanUsername).getText(), is(equalTo(username)));
    }
}
