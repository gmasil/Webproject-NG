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
package de.gmasil.webproject.jpa.video;

import java.io.Serializable;

import javax.persistence.Embeddable;

import de.gmasil.webproject.dto.VideoSeekPreviewFileDto;
import de.gmasil.webproject.dto.VideoSeekPreviewFileDto.VideoSeekPreviewFileDtoBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoSeekPreviewFile implements Serializable {

    private String name;
    private Integer width;
    private Integer height;
    private Integer frames;

    public VideoSeekPreviewFileDto toDto() {
        VideoSeekPreviewFileDtoBuilder builder = VideoSeekPreviewFileDto.builder();
        builder.name(getName());
        builder.width(getWidth());
        builder.height(getHeight());
        builder.frames(getFrames());
        return builder.build();
    }
}
