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
package de.gmasil.webproject.jpa.user;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import de.gmasil.webproject.dto.UserDto;

@Transactional
public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

    private static final String ROLES = "roles";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllWithRole(String role) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        Join<Object, Object> roles = from.join(ROLES);
        criteria.where(builder.equal(roles.get("name"), role));
        return entityManager.createQuery(criteria).getResultList();
    }

    /*
     * replace with entity graph when available in spring native
     */
    @Override
    public Optional<User> findWithRolesByUsername(String username) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        // eagerly load roles
        from.fetch(ROLES);
        criteria.where(builder.equal(from.get("username"), username));
        List<User> resultList = entityManager.createQuery(criteria).getResultList();
        if (resultList.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public Optional<User> findWithRolesById(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> from = criteria.from(User.class);
        // eagerly load roles
        from.fetch(ROLES);
        criteria.where(builder.equal(from.get("id"), id));
        List<User> resultList = entityManager.createQuery(criteria).getResultList();
        if (resultList.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    @Override
    public Optional<UserDto> findProjectionById(Long id) {
        Optional<User> optionalUser = findWithRolesById(id);
        if (!optionalUser.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new UserDto(optionalUser.get()));
    }
}
