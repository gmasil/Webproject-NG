/*
 * Webproject NG
 * Copyright © 2022 Gmasil
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
package de.gmasil.webproject.jpa.globalproperty;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Transactional
public class PropertyRepositoryExtensionImpl implements PropertyRepositoryExtension {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setProperty(String key, String value) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> criteria = builder.createQuery(Property.class);
        Root<Property> root = criteria.from(Property.class);
        criteria.where(builder.equal(root.get("key"), key));
        Property setting;
        try {
            setting = entityManager.createQuery(criteria).getSingleResult();
            setting.setValue(value);
            entityManager.persist(setting);
        } catch (NoResultException e) {
            setting = Property.builder().key(key).value(value).build();
            entityManager.persist(setting);
        }
    }
}
