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
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import de.gmasil.webproject.dto.VideoFullDto;

@Transactional
public class VideoRepositoryExtensionImpl implements VideoRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VideoFullDto> findFullProjectionById(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Video> criteria = builder.createQuery(Video.class);
        criteria.distinct(true);
        Root<Video> from = criteria.from(Video.class);
        // eagerly load other entities
        from.fetch("files", JoinType.LEFT);
        from.fetch("artists", JoinType.LEFT);
        from.fetch("categories", JoinType.LEFT);
        from.fetch("comments", JoinType.LEFT);
        from.fetch("ratings", JoinType.LEFT);
        criteria.where(builder.equal(from.get("id"), id));
        List<Video> resultList = entityManager.createQuery(criteria).getResultList();
        if (resultList.size() != 1) {
            return Optional.empty();
        }
        Video video = resultList.get(0);
        return Optional.of(video.toFullDto());
    }
}
