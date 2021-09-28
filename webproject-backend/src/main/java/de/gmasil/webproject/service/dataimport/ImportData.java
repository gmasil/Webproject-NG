/*
 * Webproject NG
 * Copyright © 2021 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Webproject NG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
 */
package de.gmasil.webproject.service.dataimport;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.gmasil.webproject.jpa.VideoQuality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportData {

    private List<ImportUser> users = new LinkedList<>();
    private List<ImportTheme> themes = new LinkedList<>();
    private List<ImportVideo> videos = new LinkedList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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
    @AllArgsConstructor
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
    @AllArgsConstructor
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
        @AllArgsConstructor
        public static class ImportFile {

            private String name;
            private VideoQuality quality;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ImportComment {

            private String username;
            private String comment;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ImportRating {

            private String username;
            private int rating;
        }
    }
}
