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
package de.gmasil.webproject.jpa.category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.GenericEntityTester;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class CategoryTest extends GenericEntityTester<Category, Video> {

    @Autowired
    public CategoryTest(CategoryRepository categoryRepo, VideoRepository videoRepo) {
        super("category", "video", categoryRepo, videoRepo);
    }

    @Transactional
    @Scenario("Persisting a category will persist attached videos")
    void testPersistCategoryAndAttachedVideos() {
        testPersistEntityAndAttachedEntities();
    }

    @Transactional
    @Scenario("When deleting a category it is detached from a video while preserving it")
    void testCategoryDeletionPreservesVideos() {
        testEntityDeletionPreservesAttachedEntities();
    }

    @Override
    public Category createEntity() {
        return Category.builder().name("Category 1").build();
    }

    @Override
    public Video createAttachedEntity() {
        return Video.builder().title("Video 1").build();
    }

    @Override
    public void attachToEntity(Category category, Video video) {
        category.addVideo(video);
    }

    @Override
    public void assertEntityHasAttachedEntity(Category category) {
        assertThat(category.getName(), is(equalTo("Category 1")));
        assertThat(category.getVideos(), hasSize(1));
        assertThat(category.getVideos().iterator().next().getTitle(), is(equalTo("Video 1")));
    }

    @Override
    public void assertAttachedEntityHasEntityAttached(Video video) {
        assertThat(video.getTitle(), is(equalTo("Video 1")));
        assertThat(video.getCategories(), hasSize(1));
        assertThat(video.getCategories().iterator().next().getName(), is(equalTo("Category 1")));
    }

    @Override
    public void assertAttachedEntityHasNothingAttached(Video video) {
        assertThat(video.getTitle(), is(equalTo("Video 1")));
        assertThat(video.getArtists(), hasSize(0));
    }
}
