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

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gmasil.webproject.controller.PermitAll;
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
    public ResponseEntity<Object> get(Pageable pageable) {
        return ResponseEntity.ok(videoRepo.findAllProjectionBy(pageable));
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        Optional<VideoFullDto> video = videoRepo.findFullProjectionById(id);
        if (video.isPresent()) {
            return ResponseEntity.ok(video.get());
        } else {
            return createVideoNotFound(id);
        }
    }

    // utils

    private ResponseEntity<Object> createVideoNotFound(Long id) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Video with id " + id + " not found");
    }
}
