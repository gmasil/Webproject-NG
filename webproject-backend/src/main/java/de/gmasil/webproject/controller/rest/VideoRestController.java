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
package de.gmasil.webproject.controller.rest;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
import de.gmasil.webproject.dto.VideoDto;
import de.gmasil.webproject.dto.VideoFullDto;
import de.gmasil.webproject.jpa.video.VideoRepository;

@RestController
@RequestMapping("/api/videos")
public class VideoRestController {

    @Autowired
    private VideoRepository videoRepo;

    @PermitAll
    @Transactional
    @GetMapping("")
    public ResponseEntity<Object> get(Pageable page) {
        return ResponseEntity.ok(videoRepo.findAllProjectionByReleaseDateNotNull(page));
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        Optional<VideoFullDto> video = videoRepo.findFullProjectionByReleasedNotNullAndId(id);
        if (video.isPresent()) {
            return ResponseEntity.ok(video.get());
        } else {
            return createVideoNotFound(id);
        }
    }

    @PermitAll
    @GetMapping("/featured")
    public ResponseEntity<Object> getFeaturedVideo() {
        VideoDto video = getFeaturedVideoDto();
        if (video == null) {
            return createVideoNotFound(null);
        }
        return ResponseEntity.ok(video);
    }

    // utils

    private VideoDto getFeaturedVideoDto() {
        Calendar calendar = Calendar.getInstance();
        int seed = calendar.get(Calendar.HOUR_OF_DAY) * 10 + (calendar.get(Calendar.MINUTE) / 10);
        Random r = new Random(seed);
        int count = (int) (videoRepo.countReleased() % Integer.MAX_VALUE);
        int selection = r.nextInt(count);
        Pageable paging = PageRequest.of(selection, 1, Sort.by("id"));
        Page<VideoDto> page = videoRepo.findAllProjectionByReleaseDateNotNull(paging);
        if (page.isEmpty()) {
            return null;
        }
        return page.getContent().get(0);
    }

    private ResponseEntity<Object> createVideoNotFound(Long id) {
        String msg;
        if (id == null) {
            msg = "No video found";
        } else {
            msg = "Video with id " + id + " not found";
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
}
