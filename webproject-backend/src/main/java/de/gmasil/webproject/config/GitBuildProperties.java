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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Component
@PropertySource("classpath:git.properties")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class GitBuildProperties {

    @JsonProperty
    private BuildProperties build;

    @JsonProperty
    private CommitProperties commit;

    @Builder
    public GitBuildProperties( //
            @Value("${git.build.time}") String timeString, //
            @Value("${git.build.version}") String version, //
            @Value("${git.commit.id}") String id, //
            @Value("${git.commit.id.abbrev}") String abbrev, //
            @Value("${git.commit.id.describe}") String describe, //
            @Value("${git.commit.id.describe-short}") String describeShort, //
            @Value("${git.dirty}") boolean dirty //
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        Date time = Date.from(ZonedDateTime.parse(timeString, formatter).toInstant());
        build = new BuildProperties(time, version);
        commit = new CommitProperties(id, abbrev, describe, describeShort, dirty);
    }

    @JsonCreator
    public static GitBuildProperties create() {
        return GitBuildProperties.builder().build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildProperties {

        private Date time;
        private String version;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommitProperties {

        private String id;
        private String abbrev;
        private String describe;
        private String describeShort;
        private boolean dirty;
    }
}
