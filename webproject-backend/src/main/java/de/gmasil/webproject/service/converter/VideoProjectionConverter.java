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
package de.gmasil.webproject.service.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.newprojection.VideoProjectionNEW;

@Service
public class VideoProjectionConverter implements Converter<Video, VideoProjectionNEW> {

    @Override
    public VideoProjectionNEW convert(Video video) {
        return new VideoProjectionNEW(video);
    }
}
