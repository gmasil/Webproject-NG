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
package de.gmasil.webproject.service.dataimport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.GherkinTest;
import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.artist.ArtistRepository;
import de.gmasil.webproject.jpa.category.CategoryRepository;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videofile.VideoFileRepository;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.utils.SetupTestContext;
import de.gmasil.webproject.utils.extension.EnableTestDataImport;

@SetupTestContext
@EnableTestDataImport
@Story("Data import is tested")
class DataImportServiceTest extends GherkinTest {

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private ThemeRepository themeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private VideoFavoriteRepository favoriteRepo;

    @Autowired
    private VideoFileRepository fileRepo;

    @Autowired
    private VideoRatingRepository ratingRepo;

    @Scenario("Data is imported from file upon startup")
    void testForExistingData() throws IOException {
        when("data is imported on startup", () -> {
        });
        then("all repositories contain the expected data", () -> {
            assertThat(artistRepo.count(), is(equalTo(3L)));
            assertThat(categoryRepo.count(), is(equalTo(3L)));
            assertThat(commentRepo.count(), is(equalTo(2L)));
            assertThat(propertyRepo.count(), is(equalTo(1L)));
            assertThat(roleRepo.count(), is(equalTo(2L)));
            assertThat(themeRepo.count(), is(equalTo(3L)));
            assertThat(userRepo.count(), is(equalTo(2L)));
            assertThat(videoRepo.count(), is(equalTo(2L)));
            assertThat(favoriteRepo.count(), is(equalTo(2L)));
            assertThat(fileRepo.count(), is(equalTo(3L)));
            assertThat(ratingRepo.count(), is(equalTo(2L)));
        });
    }

    @Scenario("Video duration is picked up from length and duration")
    void testAliasVideoLengthDuration() throws IOException {
        when("data is imported on startup", () -> {
        });
        then("the videos have the correct duration attribute", () -> {
            assertThat(videoRepo.count(), is(equalTo(2L)));
            // first video
            final String title1 = "The adventure in the middle of an adventure";
            Optional<Video> video1 = videoRepo.findAll().stream().filter(v -> v.getTitle().equals(title1)).findFirst();
            assertThat(video1.isPresent(), is(equalTo(true)));
            assertThat(video1.get().getTitle(), is(equalTo("The adventure in the middle of an adventure")));
            assertThat(video1.get().getDuration(), is(equalTo(13.5f)));
            assertThat(video1.get().getSeekPreviewFile(), is(notNullValue()));
            assertThat(video1.get().getSeekPreviewFile().getName(), is(equalTo("testfile.jpg")));
            assertThat(video1.get().getSeekPreviewFile().getWidth(), is(equalTo(192)));
            assertThat(video1.get().getSeekPreviewFile().getHeight(), is(equalTo(108)));
            assertThat(video1.get().getSeekPreviewFile().getFrames(), is(equalTo(300)));
            // second video
            final String title2 = "Movie Part 2";
            Optional<Video> video2 = videoRepo.findAll().stream().filter(v -> v.getTitle().equals(title2)).findFirst();
            assertThat(video2.isPresent(), is(equalTo(true)));
            assertThat(video2.get().getTitle(), is(equalTo("Movie Part 2")));
            assertThat(video2.get().getDuration(), is(equalTo(8.3f)));
            assertThat(video2.get().getSeekPreviewFile(), is(nullValue()));
        });
    }
}
