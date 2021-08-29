package de.gmasil.webproject.jpa.comment;

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
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class CommentTest {

    @Nested
    class CommentVideoTest extends GenericEntityTester<Comment, Video> {

        @Autowired
        public CommentVideoTest(CommentRepository commentRepo, VideoRepository videoRepo) {
            super("comment", "video", commentRepo, videoRepo);
        }

        @Transactional
        @Scenario("Persisting a comment will persist attached videos")
        void testPersistCommentAndAttachedVideos() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a comment it is detached from a video while preserving it")
        void testCommentDeletionPreservesVideos() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Comment createEntity() {
            return Comment.builder().text("Comment 1").build();
        }

        @Override
        public Video createAttachedEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public void attachToEntity(Comment comment, Video video) {
            comment.setVideo(video);
        }

        @Override
        public void assertEntityHasAttachedEntity(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getVideo(), is(not(nullValue())));
            assertThat(comment.getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getComments(), hasSize(1));
            assertThat(video.getComments().iterator().next().getText(), is(equalTo("Comment 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getComments(), hasSize(0));
        }
    }

    @Nested
    class CommentUserTest extends GenericEntityTester<Comment, User> {

        @Autowired
        public CommentUserTest(CommentRepository commentRepo, UserRepository userRepo) {
            super("comment", "user", commentRepo, userRepo);
        }

        @Transactional
        @Scenario("Persisting a comment will persist attached Users")
        void testPersistCommentAndAttachedUsers() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a comment it is detached from a user while preserving it")
        void testCommentDeletionPreservesUsers() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Comment createEntity() {
            return Comment.builder().text("Comment 1").build();
        }

        @Override
        public User createAttachedEntity() {
            return User.builder().username("User 1").password("pass1").build();
        }

        @Override
        public void attachToEntity(Comment comment, User user) {
            comment.setUser(user);
        }

        @Override
        public void assertEntityHasAttachedEntity(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getUser(), is(not(nullValue())));
            assertThat(comment.getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getComments(), hasSize(1));
            assertThat(user.getComments().iterator().next().getText(), is(equalTo("Comment 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(User user) {
            assertThat(user.getUsername(), is(equalTo("User 1")));
            assertThat(user.getComments(), hasSize(0));
        }
    }
}
