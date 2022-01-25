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
