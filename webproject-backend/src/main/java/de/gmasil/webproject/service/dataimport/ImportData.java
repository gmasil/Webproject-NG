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
package de.gmasil.webproject.service.dataimport;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImportData {

    private List<ImportUser> users = new LinkedList<>();
    private List<ImportTheme> themes = new LinkedList<>();
    private List<ImportVideo> videos = new LinkedList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ImportUser {

        private String username;
        private String password;
        private List<String> roles = new LinkedList<>();
        @JsonProperty("active-theme")
        private String activeTheme;
        private List<ImportTheme> themes = new LinkedList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ImportTheme {

        private String name;
        @JsonProperty("background-color")
        private Color backgroundColor;
        @JsonProperty("background-highlight-color")
        private Color backgroundHighlightColor;
        @JsonProperty("primary-color")
        private Color primaryColor;
        @JsonProperty("secondary-color")
        private Color secondaryColor;
        @JsonProperty("text-color")
        private Color textColor;
        private boolean preset = false;
        @JsonProperty("default")
        private boolean isDefault = false;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImportVideo {

        private String title;
        private String description;
        private float length;
        private String thumbnail;
        private List<ImportFile> files = new LinkedList<>();
        private List<String> artists = new LinkedList<>();
        private List<String> categories = new LinkedList<>();
        private List<ImportComment> comments = new LinkedList<>();
        private List<String> favoriters = new LinkedList<>();
        private List<ImportRating> ratings = new LinkedList<>();

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ImportFile {

            private String name;
            private String quality;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ImportComment {

            private String username;
            private String comment;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class ImportRating {

            private String username;
            private int rating;
        }
    }
}
