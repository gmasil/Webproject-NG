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
