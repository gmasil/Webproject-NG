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
package de.gmasil.webproject.jpa.videofile;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Reference;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class VideoFileTest extends GherkinTest {

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private VideoFileRepository videoFileRepo;

    @Transactional
    @Scenario("When deleting a file it is detached from a video while preserving it")
    void testFileDeletionPreservesVideo(Reference<Video> video, Reference<VideoFile> videoFile) {
        given("a video exists", () -> {
            video.set(videoRepo.save(Video.builder().title("Video 1").build()));
        });
        and("a file attached to the video exists", () -> {
            videoFile.set(VideoFile.builder().name("File 1").quality("HD").build());
            videoFile.get().addVideo(video.get());
            videoFile.set(videoFileRepo.save(videoFile.get()));
        });
        when("the file is removed", () -> {
            videoFileRepo.deleteAll();
        });
        then("the file does not exist anymore", () -> {
            assertThat(videoFileRepo.count(), is(equalTo(0L)));
        });
        and("the video still exists without files", () -> {
            assertThat(videoRepo.count(), is(equalTo(1L)));
            assertThat(videoRepo.findAll().get(0).getFiles(), hasSize(0));
        });
    }
}
