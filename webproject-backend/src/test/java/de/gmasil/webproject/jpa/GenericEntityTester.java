package de.gmasil.webproject.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.data.jpa.repository.JpaRepository;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;

public abstract class GenericEntityTester<T, E> extends GherkinTest {

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
            attachedEntity.set(createAttachedEntity());
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
}
