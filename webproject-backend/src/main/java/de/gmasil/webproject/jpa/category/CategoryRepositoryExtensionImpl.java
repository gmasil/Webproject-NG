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
package de.gmasil.webproject.jpa.category;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryExtensionImpl implements CategoryRepositoryExtension {

    @Autowired
    private EntityManager entityManager;

    @Override
    public CategoryDAO findByNameOrCreate(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryDAO> criteria = builder.createQuery(CategoryDAO.class);
        Root<CategoryDAO> root = criteria.from(CategoryDAO.class);
        criteria.where(builder.equal(root.get("name"), name));
        try {
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            CategoryDAO category = new CategoryDAO(name);
            entityManager.persist(category);
            return category;
        }
    }
}
