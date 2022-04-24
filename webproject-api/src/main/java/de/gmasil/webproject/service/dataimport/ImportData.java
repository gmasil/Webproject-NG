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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImportData {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss z";

    private @Default ImportPaths paths = new ImportPaths();
    private @Default List<ImportUser> users = new LinkedList<>();
    private @Default List<ImportTheme> themes = new LinkedList<>();
    private @Default List<ImportVideo> videos = new LinkedList<>();

    public ImportData addUser(ImportUser user) {
        users.add(user);
        return this;
    }

    public ImportData addTheme(ImportTheme theme) {
        themes.add(theme);
        return this;
    }

    public ImportData addVideo(ImportVideo video) {
        videos.add(video);
        return this;
    }

    public void removeEmpty() {
        paths.removeEmpty();
        if (paths.isEmpty()) {
            paths = null;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImportPaths {

        private @Default ImportPrefix prefix = new ImportPrefix();

        public void removeEmpty() {
            prefix.removeEmpty();
            if (prefix.isEmpty()) {
                prefix = null;
            }
        }

        public boolean isEmpty() {
            return prefix == null;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportPrefix {

            private @Default String video = "";
            private @Default String thumbnail = "";
            private @Default String videoPreview = "";

            public void removeEmpty() {
                if (isEmpty(video)) {
                    video = null;
                }
                if (isEmpty(thumbnail)) {
                    thumbnail = null;
                }
                if (isEmpty(videoPreview)) {
                    videoPreview = null;
                }
            }

            public boolean isEmpty() {
                return video == null && thumbnail == null && videoPreview == null;
            }

            private boolean isEmpty(String s) {
                return s == null || s.trim().isEmpty();
            }
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImportUser {

        private String username;
        private String password;
        private @Default List<String> roles = new LinkedList<>();
        private @JsonProperty("active-theme") String activeTheme;
        private @Default List<ImportTheme> themes = new LinkedList<>();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImportTheme {

        private String name;
        private @JsonProperty("background-color") Color backgroundColor;
        private @JsonProperty("background-highlight-color") Color backgroundHighlightColor;
        private @JsonProperty("primary-color") Color primaryColor;
        private @JsonProperty("secondary-color") Color secondaryColor;
        private @JsonProperty("text-color") Color textColor;
        private @Default boolean preset = false;
        private @Default @JsonProperty("default") boolean isDefault = false;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ImportVideo {

        private String title;
        private String description;
        private String origin;
        @JsonAlias({ "duration", "length" })
        private float duration;
        private String thumbnail;
        private String studio;
        private @Default String released = "now";
        private @Default List<ImportFile> files = new LinkedList<>();
        private @Default List<String> artists = new LinkedList<>();
        private @Default List<String> categories = new LinkedList<>();
        private @Default List<ImportComment> comments = new LinkedList<>();
        private @Default List<String> favoriters = new LinkedList<>();
        private @Default List<ImportRating> ratings = new LinkedList<>();
        private @Default List<ImportScene> scenes = new LinkedList<>();
        @JsonAlias({ "seekPreviewFile", "seek-preview-file", "seekpreviewfile" })
        private ImportSeekPreviewFile seekPreviewFile;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportFile {

            private String name;
            private String quality;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportComment {

            private String username;
            private String comment;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportRating {

            private String username;
            private int rating;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportScene {

            private String name;
            private float time;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class ImportSeekPreviewFile {

            private String name;
            private int width;
            private int height;
            private int frames;
        }
    }
}
