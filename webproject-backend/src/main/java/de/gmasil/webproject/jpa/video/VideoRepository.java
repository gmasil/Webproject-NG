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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.gmasil.webproject.dto.VideoDto;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoRepositoryExtension {

    public Page<VideoDto> findAllProjectionBy(Pageable pageable);

    public Page<VideoDto> findAllProjectionByReleaseDateNotNull(Pageable pageable);

    @Query("select id from #{#entityName}")
    public List<Long> findAllIds();

    @Query("select count(*) from VIDEO where releaseDate IS NOT NULL")
    public Long countReleased();
}
