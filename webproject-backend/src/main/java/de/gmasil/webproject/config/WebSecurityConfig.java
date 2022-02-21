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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import de.gmasil.webproject.jpa.user.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Lazy
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    @Autowired
    private AppProperties app;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.headers().frameOptions().sameOrigin();

        // do not redirect unauthorized /api/** requests to /login
        http.exceptionHandling().defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                new AntPathRequestMatcher("/api/**"));

        if (app.isPublicAccess()) {
            http.authorizeRequests().anyRequest().permitAll();
        } else {
            http.authorizeRequests() //
                    .antMatchers("/static/**").permitAll() //
                    .antMatchers("/api/users/current", "/api/app/config").permitAll() //
                    .antMatchers("/api/themes/active.css", "/api/themes/active").permitAll() //
                    .antMatchers("/login", "/logout", "/performlogin").permitAll() //
                    .anyRequest().authenticated();
        }

        http.formLogin().loginPage("/login").failureUrl("/login?error") //
                .loginProcessingUrl("/performlogin") //
                .usernameParameter("username") //
                .passwordParameter("password");
        http.logout() //
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //
                .logoutSuccessUrl("/login?logout") //
                .deleteCookies("JSESSIONID", "rememberme");
        http.rememberMe() //
                .key("uniqueAndSecret") //
                .userDetailsService(userService) //
                .rememberMeParameter("rememberme");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
