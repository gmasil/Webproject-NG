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
package de.gmasil.webproject.jpa.user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.gherkin.extension.Scenario;
import de.gmasil.gherkin.extension.Story;
import de.gmasil.webproject.jpa.GenericEntityTester;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavorite;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videorating.VideoRating;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
public class UserTest {

    @Nested
    @SetupTestContext
    class UserRoleTest extends GenericEntityTester<User, Role> {

        @Autowired
        public UserRoleTest(UserRepository userRepo, RoleRepository roleRepo) {
            super("user", "role", userRepo, roleRepo);
        }

        @Transactional
        @Scenario("Persisting a user will persist attached roles")
        void testPersistUserAndAttachedRoles() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a user it is detached from a role while preserving it")
        void testUserDeletionPreservesRole() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public User createEntity() {
            return User.builder().username("User 1").password("pass1").build();
        }

        @Override
        public Role createAttachedEntity() {
            return Role.builder().name("Role 1").build();
        }

        @Override
        public void attachToEntity(User user, Role role) {
            user.addRole(role);
        }

        @Override
        public void assertEntityHasAttachedEntity(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getRoles(), hasSize(1));
            assertThat(user.getRoles().iterator().next().getName(), is(equalTo("Role 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Role role) {
            assertThat(role.getName(), is(equalTo("Role 1")));
            assertThat(role.getUsers(), hasSize(1));
            assertThat(role.getUsers().iterator().next().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Role role) {
            assertThat(role.getName(), is(equalTo("Role 1")));
            assertThat(role.getUsers(), hasSize(0));
        }
    }

    @Nested
    @SetupTestContext
    class UserCommentTest extends GenericEntityTester<User, Comment> {

        @Autowired
        public UserCommentTest(UserRepository userRepo, CommentRepository commentRepo) {
            super("user", "role", userRepo, commentRepo);
        }

        @Transactional
        @Scenario("Persisting a user will persist attached comments")
        void testPersistUserAndAttachedComments() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a user it is detached from a comment while preserving it")
        void testUserDeletionPreservesComment() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public User createEntity() {
            return User.builder().username("User 1").password("pass1").build();
        }

        @Override
        public Comment createAttachedEntity() {
            return Comment.builder().text("Comment 1").build();
        }

        @Override
        public void attachToEntity(User user, Comment comment) {
            user.addComment(comment);
        }

        @Override
        public void assertEntityHasAttachedEntity(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getComments(), hasSize(1));
            assertThat(user.getComments().iterator().next().getText(), is(equalTo("Comment 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getUser(), is(not(nullValue())));
            assertThat(comment.getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getUser(), is(nullValue()));
        }
    }

    @Nested
    @SetupTestContext
    class UserVideoFavoriteTest extends GenericEntityTester<User, VideoFavorite> {

        @Autowired
        private VideoRepository videoRepo;

        @Autowired
        public UserVideoFavoriteTest(UserRepository userRepo, VideoFavoriteRepository favoriteRepo) {
            super("user", "favorite", userRepo, favoriteRepo);
        }

        @Transactional
        @Scenario("Persisting a user will persist attached favorites")
        void testPersistUserAndAttachedFavorites() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a user its attachted favorites are removed as well")
        void testUserDeletionRemovesFavorite() {
            testEntityDeletionRemovesAttachedEntities();
        }

        @Override
        public User createEntity() {
            return User.builder().username("User 1").password("pass1").build();
        }

        @Override
        public VideoFavorite createAttachedEntity() {
            Video video = Video.builder().title("Video 1").build();
            video = videoRepo.save(video);
            return VideoFavorite.builder().video(video).build();
        }

        @Override
        public void attachToEntity(User user, VideoFavorite favorite) {
            user.addFavorite(favorite);
        }

        @Override
        public void assertEntityHasAttachedEntity(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getFavorites(), hasSize(1));
            assertThat(user.getFavorites().iterator().next().getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(VideoFavorite favorite) {
            assertThat(favorite.getVideo().getTitle(), is(equalTo("Video 1")));
            assertThat(favorite.getUser(), is(not(nullValue())));
            assertThat(favorite.getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(VideoFavorite favorite) {
            assertThat(favorite.getVideo().getTitle(), is(equalTo("Video 1")));
            assertThat(favorite.getUser(), is(nullValue()));
        }
    }

    @Nested
    @SetupTestContext
    class UserVideoRatingTest extends GenericEntityTester<User, VideoRating> {

        @Autowired
        private VideoRepository videoRepo;

        @Autowired
        public UserVideoRatingTest(UserRepository userRepo, VideoRatingRepository ratingRepo) {
            super("user", "rating", userRepo, ratingRepo);
        }

        @Transactional
        @Scenario("Persisting a user will persist attached ratings")
        void testPersistUserAndAttachedRatings() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a user it is detached from a rating while preserving it")
        void testUserDeletionPreservesRating() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public User createEntity() {
            return User.builder().username("User 1").password("pass1").build();
        }

        @Override
        public VideoRating createAttachedEntity() {
            Video video = Video.builder().title("Video 1").build();
            video = videoRepo.save(video);
            return VideoRating.builder().rating(3).video(video).build();
        }

        @Override
        public void attachToEntity(User user, VideoRating rating) {
            user.addRating(rating);
        }

        @Override
        public void assertEntityHasAttachedEntity(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getRatings(), hasSize(1));
            assertThat(user.getRatings().iterator().next().getRating(), is(equalTo(3)));
            assertThat(user.getRatings().iterator().next().getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(VideoRating rating) {
            assertThat(rating.getRating(), is(equalTo(3)));
            assertThat(rating.getVideo().getTitle(), is(equalTo("Video 1")));
            assertThat(rating.getUser(), is(not(nullValue())));
            assertThat(rating.getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(VideoRating rating) {
            assertThat(rating.getRating(), is(equalTo(3)));
            assertThat(rating.getVideo().getTitle(), is(equalTo("Video 1")));
            assertThat(rating.getUser(), is(nullValue()));
        }
    }
}
