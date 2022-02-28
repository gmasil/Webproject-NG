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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.dto.UserDto;
import de.gmasil.webproject.dto.UserPasswordDto;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extension.EnableTestDataImport;
import de.gmasil.webproject.utils.resttemplate.AdvRestTemplate;
import de.gmasil.webproject.utils.resttemplate.RestTemplateFactory;

@SetupTestContext
@EnableTestDataImport
@Story("Test user REST endpoint")
class UserRestControllerTest extends GherkinTest {

    @Autowired
    private RestTemplateFactory factory;

    @Scenario("Anonymous users receive null for current user")
    void testAnonymousUserNullForCurrentUser(Reference<UserDto> user) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("the user is not authenticated", () -> {
        });
        when("the endpoint /api/users/current is called", () -> {
            user.set(rest.getForObject("/api/users/current", UserDto.class));
        });
        then("null is returned", () -> {
            assertThat(user.get(), is(nullValue()));
        });
    }

    @Scenario("Authenticated users receive their current information")
    void testAuthenticatedUsersGetCorrectInfo(Reference<UserDto> user) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("the user is authenticated", () -> {
            rest.loginUser();
        });
        when("the endpoint /api/users/current is called", () -> {
            user.set(rest.getForObject("/api/users/current", UserDto.class));
        });
        then("the correct user info is returned", () -> {
            assertThat(user.get(), is(not(nullValue())));
            assertThat(user.get().getUsername(), is(not(nullValue())));
            assertThat(user.get().getRoles(), hasSize(greaterThan(0)));
        });
    }

    @Scenario("Authenticated users can change their password")
    void testAuthenticatedUserCanChangePassword() {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("the user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user changes his password", () -> {
            rest.put("/api/users/updatepassword",
                    UserPasswordDto.builder().currentPassword("user").newPassword("s3cret").build());
        });
        then("the user can only login with the new password", () -> {
            rest.logout();
            rest.loginUser("s3cret");
            UserDto user = rest.getForObject("/api/users/current", UserDto.class);
            assertThat(user, is(not(nullValue())));
            assertThat(user.getUsername(), is(not(nullValue())));
            assertThat(user.getRoles(), hasSize(greaterThan(0)));
        });
    }

    @Scenario("Password cannot be changed when current password is incorrect")
    void testPasswordMustBeCorrectToChange(Reference<Exception> exception) {
        AdvRestTemplate rest = factory.createRestTemplate();
        given("the user is authenticated", () -> {
            rest.loginUser();
        });
        when("the user tries to change his password with wrong current password", () -> {
            try {
                rest.put("/api/users/updatepassword", UserPasswordDto.builder()
                        .currentPassword("this-is-not-my-password").newPassword("s3cret").build());
            } catch (Exception e) {
                exception.set(e);
            }
        });
        then("an error 400 is shown", () -> {
            assertThat(exception.get(), is(not(nullValue())));
            assertThat(exception.get().getMessage(), containsString("Current password does not match"));
        });
        and("the user can only login with the initial password", () -> {
            rest.logout();
            rest.loginUser();
            UserDto user = rest.getForObject("/api/users/current", UserDto.class);
            assertThat(user, is(not(nullValue())));
            assertThat(user.getUsername(), is(not(nullValue())));
            assertThat(user.getRoles(), hasSize(greaterThan(0)));
        });
    }
}
