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
package de.gmasil.webproject.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;

public abstract class GenericEntityTester<T extends Auditable, E extends Auditable> extends GherkinTest {

    private String entityName;
    private String attachedEntityName;

    private JpaRepository<T, Long> entityRepo;
    private JpaRepository<E, Long> attachedEntityRepo;

    public GenericEntityTester(String entityName, String attachedEntityName, JpaRepository<T, Long> entityRepo,
            JpaRepository<E, Long> attachedEntityRepo) {
        this.entityName = entityName;
        this.attachedEntityName = attachedEntityName;
        this.entityRepo = entityRepo;
        this.attachedEntityRepo = attachedEntityRepo;
    }

    public abstract T createEntity();

    public abstract E createAttachedEntity();

    public abstract void attachToEntity(T entity, E attachedEntity);

    public abstract void assertEntityHasAttachedEntity(T entity);

    public abstract void assertAttachedEntityHasEntityAttached(E attachedEntity);

    public abstract void assertAttachedEntityHasNothingAttached(E attachedEntity);

    public void testPersistEntityAndAttachedEntities() {
        Reference<T> entity = new Reference<>();
        Reference<E> attachedEntity = new Reference<>();
        given("an unpersisted " + attachedEntityName + " exists", () -> {
            attachedEntity.set(createAttachedEntity());
        });
        and("an unpersisted " + entityName + " exists with the " + attachedEntityName + " attached", () -> {
            entity.set(createEntity());
            attachToEntity(entity.get(), attachedEntity.get());
        });
        when("the " + entityName + " is persisted", () -> {
            entityRepo.save(entity.get());
        });
        then("the attached " + attachedEntityName + " is persisted as well", () -> {
            assertThat(entityRepo.count(), is(equalTo(1L)));
            assertThat(attachedEntityRepo.count(), is(equalTo(1L)));
        });
        and("the " + entityName + " has the " + attachedEntityName + " attached", () -> {
            assertEntityHasAttachedEntity(entityRepo.findAll().get(0));
        });
        and("the " + attachedEntityName + " has the " + entityName + " attached", () -> {
            assertAttachedEntityHasEntityAttached(attachedEntityRepo.findAll().get(0));
        });
    }

    public void testEntityDeletionPreservesAttachedEntities() {
        Reference<T> entity = new Reference<>();
        Reference<E> attachedEntity = new Reference<>();
        given("a persisted " + attachedEntityName + " exists", () -> {
            attachedEntity.set(attachedEntityRepo.save(createAttachedEntity()));
        });
        and("a persisted " + entityName + " exists with the " + attachedEntityName + " attached", () -> {
            entity.set(createEntity());
            attachToEntity(entity.get(), attachedEntity.get());
            entity.set(entityRepo.save(entity.get()));
        });
        when("the " + entityName + " is deleted", () -> {
            entityRepo.delete(entity.get());
        });
        then("the " + attachedEntityName + " still exists", () -> {
            assertThat(attachedEntityRepo.count(), is(equalTo(1L)));
        });
        and("the " + attachedEntityName + " has no " + entityName + " attached", () -> {
            assertAttachedEntityHasNothingAttached(attachedEntityRepo.findAll().get(0));
        });
        and("the " + entityName + " was removed", () -> {
            assertThat(entityRepo.count(), is(equalTo(0L)));
        });
    }

    public void testEntityDeletionRemovesAttachedEntities() {
        Reference<T> entity = new Reference<>();
        Reference<E> attachedEntity = new Reference<>();
        given("a persisted " + attachedEntityName + " exists", () -> {
            attachedEntity.set(createAttachedEntity());
        });
        and("a persisted " + entityName + " exists with the " + attachedEntityName + " attached", () -> {
            entity.set(createEntity());
            attachToEntity(entity.get(), attachedEntity.get());
            entity.set(entityRepo.save(entity.get()));
        });
        when("the " + entityName + " is deleted", () -> {
            entityRepo.delete(entity.get());
        });
        then("the " + entityName + " was removed", () -> {
            assertThat(entityRepo.count(), is(equalTo(0L)));
        });
        and("the " + attachedEntityName + " was deleted as well", () -> {
            assertThat(attachedEntityRepo.count(), is(equalTo(0L)));
        });
    }
}
