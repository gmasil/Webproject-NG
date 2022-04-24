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
package de.gmasil.webproject.config;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public AuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        setRedirectStrategy(new DefaultRedirectStrategy() {

            @Override
            public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
                    throws IOException {

                List<String> params = new LinkedList<>();
                params.add("error");
                String username = request.getParameter(WebSecurityConfig.USERNAME_PARAMETER);
                if (username != null && !username.isEmpty()) {
                    params.add(WebSecurityConfig.USERNAME_PARAMETER + "=" + username);
                }
                String rememberme = request.getParameter(WebSecurityConfig.REMEMBER_ME_PARAMETER);
                if (rememberme != null && !rememberme.isEmpty()) {
                    params.add(WebSecurityConfig.REMEMBER_ME_PARAMETER + "=" + rememberme);
                }
                if (!params.isEmpty()) {
                    url += "?" + String.join("&", params);
                }
                super.sendRedirect(request, response, url);
            }

        });
    }
}
