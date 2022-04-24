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
package de.gmasil.webproject.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VideoFullDto {

    private Long id;
    private String title;
    private String description;
    private float duration;
    private String thumbnail;
    private String thumbnailPreview;
    private Date releaseDate;
    private @Default Set<VideoFileDto> files = new HashSet<>();
    private @Default Set<ArtistDto> artists = new HashSet<>();
    private @Default Set<String> categories = new HashSet<>();
    private @Default Set<CommentDto> comments = new HashSet<>();
    private float rating;
    private @Default Set<SceneDto> scenes = new HashSet<>();
    private VideoSeekPreviewFileDto seekPreviewFile;

    @JsonCreator
    public static VideoFullDto create() {
        return VideoFullDto.builder().build();
    }
}
