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
package de.gmasil.webproject.jpa.videorating;

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
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class VideoRatingTest extends GherkinTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private VideoRatingRepository videoRatingRepo;

    @Transactional
    @Scenario("When deleting a rating it is detached from a video while preserving it")
    void testRatingDeletionPreservesVideo(Reference<User> user, Reference<Video> video, Reference<VideoRating> rating) {
        given("a user exists", () -> {
            user.set(userRepo.save(User.builder().username("Peter").password("pass").build()));
        });
        and("a video exists", () -> {
            video.set(videoRepo.save(Video.builder().title("Video 1").build()));
        });
        and("the user gave the video a rating of 4", () -> {
            rating.set(
                    videoRatingRepo.save(VideoRating.builder().rating(4).user(user.get()).video(video.get()).build()));
        });
        when("the rating is removed", () -> {
            videoRatingRepo.deleteAll();
        });
        then("the favorite does not exist anymore", () -> {
            assertThat(videoRatingRepo.count(), is(equalTo(0L)));
        });
        and("the user still exists without ratings", () -> {
            assertThat(userRepo.count(), is(equalTo(1L)));
            assertThat(userRepo.findAll().get(0).getRatings(), hasSize(0));
        });
        and("the video still exists without ratings", () -> {
            assertThat(videoRepo.count(), is(equalTo(1L)));
            assertThat(videoRepo.findAll().get(0).getRatings(), hasSize(0));
        });
    }
}