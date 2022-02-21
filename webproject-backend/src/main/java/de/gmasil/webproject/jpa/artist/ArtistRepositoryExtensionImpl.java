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
package de.gmasil.webproject.jpa.artist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Transactional
public class ArtistRepositoryExtensionImpl implements ArtistRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Artist findByNameOrCreate(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Artist> criteria = builder.createQuery(Artist.class);
        criteria.distinct(true);
        Root<Artist> root = criteria.from(Artist.class);
        criteria.where(builder.equal(root.get("name"), name));
        List<Artist> resultList = entityManager.createQuery(criteria).getResultList();
        if (resultList.isEmpty()) {
            Artist artist = Artist.builder().name(name).build();
            entityManager.persist(artist);
            return artist;
        } else {
            return resultList.get(0);
        }
    }
}
