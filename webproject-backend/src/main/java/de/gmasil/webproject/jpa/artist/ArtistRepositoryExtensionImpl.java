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
package de.gmasil.webproject.jpa.artist;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ArtistRepositoryExtensionImpl implements ArtistRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Artist findByNameOrCreate(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Artist> criteria = builder.createQuery(Artist.class);
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
