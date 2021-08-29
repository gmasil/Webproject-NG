package de.gmasil.webproject.jpa.video;

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
import de.gmasil.webproject.jpa.artist.Artist;
import de.gmasil.webproject.jpa.artist.ArtistRepository;
import de.gmasil.webproject.jpa.category.Category;
import de.gmasil.webproject.jpa.category.CategoryRepository;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavorite;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videofile.VideoFile;
import de.gmasil.webproject.jpa.videofile.VideoFileRepository;
import de.gmasil.webproject.jpa.videorating.VideoRating;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.utils.SetupTestContext;

@SetupTestContext
@Story("Testing database operation behaviour")
class VideoTest {

    @Nested
    class VideoVideoFileTest extends GenericEntityTester<Video, VideoFile> {

        @Autowired
        public VideoVideoFileTest(VideoRepository videoRepo, VideoFileRepository fileRepo) {
            super("video", "file", videoRepo, fileRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached files")
        void testPersistVideoAndAttachedFiles() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video it is detached from a file while preserving it")
        void testVideoDeletionPreservesFile() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public VideoFile createAttachedEntity() {
            return VideoFile.builder().name("/file1").build();
        }

        @Override
        public void attachToEntity(Video video, VideoFile file) {
            video.addFile(file);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getFiles(), hasSize(1));
            assertThat(video.getFiles().iterator().next().getName(), is(equalTo("/file1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(VideoFile file) {
            assertThat(file.getName(), is(equalTo("/file1")));
            assertThat(file.getVideos(), hasSize(1));
            assertThat(file.getVideos().iterator().next().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(VideoFile file) {
            assertThat(file.getName(), is(equalTo("/file1")));
            assertThat(file.getVideos(), hasSize(0));
        }
    }

    @Nested
    class VideoArtistTest extends GenericEntityTester<Video, Artist> {

        @Autowired
        public VideoArtistTest(VideoRepository videoRepo, ArtistRepository artistRepo) {
            super("video", "artist", videoRepo, artistRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached artists")
        void testPersistVideoAndAttachedArtists() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video it is detached from an artist while preserving it")
        void testVideoDeletionPreservesArtist() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public Artist createAttachedEntity() {
            return Artist.builder().name("Artist 1").build();
        }

        @Override
        public void attachToEntity(Video video, Artist artist) {
            video.addArtist(artist);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getArtists(), hasSize(1));
            assertThat(video.getArtists().iterator().next().getName(), is(equalTo("Artist 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Artist artist) {
            assertThat(artist.getName(), is(equalTo("Artist 1")));
            assertThat(artist.getVideos(), hasSize(1));
            assertThat(artist.getVideos().iterator().next().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Artist artist) {
            assertThat(artist.getName(), is(equalTo("Artist 1")));
            assertThat(artist.getVideos(), hasSize(0));
        }
    }

    @Nested
    class VideoCategoryTest extends GenericEntityTester<Video, Category> {

        @Autowired
        public VideoCategoryTest(VideoRepository videoRepo, CategoryRepository categoryRepo) {
            super("video", "category", videoRepo, categoryRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached categories")
        void testPersistVideoAndAttachedCategory() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video it is detached from a category while preserving it")
        void testVideoDeletionPreservesCategory() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public Category createAttachedEntity() {
            return Category.builder().name("Category 1").build();
        }

        @Override
        public void attachToEntity(Video video, Category category) {
            video.addCategory(category);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getCategories(), hasSize(1));
            assertThat(video.getCategories().iterator().next().getName(), is(equalTo("Category 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Category category) {
            assertThat(category.getName(), is(equalTo("Category 1")));
            assertThat(category.getVideos(), hasSize(1));
            assertThat(category.getVideos().iterator().next().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Category category) {
            assertThat(category.getName(), is(equalTo("Category 1")));
            assertThat(category.getVideos(), hasSize(0));
        }
    }

    @Nested
    class VideoCommentTest extends GenericEntityTester<Video, Comment> {

        @Autowired
        public VideoCommentTest(VideoRepository videoRepo, CommentRepository commentRepo) {
            super("video", "comment", videoRepo, commentRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached comments")
        void testPersistVideoAndAttachedComment() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video it is detached from a comment while preserving it")
        void testVideoDeletionPreservesComment() {
            testEntityDeletionPreservesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public Comment createAttachedEntity() {
            return Comment.builder().text("Comment 1").build();
        }

        @Override
        public void attachToEntity(Video video, Comment comment) {
            video.addComment(comment);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getComments(), hasSize(1));
            assertThat(video.getComments().iterator().next().getText(), is(equalTo("Comment 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getVideo(), is(not(nullValue())));
            assertThat(comment.getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(Comment comment) {
            assertThat(comment.getText(), is(equalTo("Comment 1")));
            assertThat(comment.getVideo(), is(nullValue()));
        }
    }

    @Nested
    class VideoVideoFavoriteTest extends GenericEntityTester<Video, VideoFavorite> {

        @Autowired
        private UserRepository userRepo;

        @Autowired
        public VideoVideoFavoriteTest(VideoRepository videoRepo, VideoFavoriteRepository favoriteRepo) {
            super("video", "favorite", videoRepo, favoriteRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached favorites")
        void testPersistVideoAndAttachedFavorite() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video its favorites are deleted as well")
        void testVideoDeletionRemovesFavorite() {
            testEntityDeletionRemovesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public VideoFavorite createAttachedEntity() {
            User user = User.builder().username("User 1").password("pass1").build();
            user = userRepo.save(user);
            return VideoFavorite.builder().user(user).build();
        }

        @Override
        public void attachToEntity(Video video, VideoFavorite favorite) {
            video.addFavorite(favorite);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getFavorites(), hasSize(1));
            assertThat(video.getFavorites().iterator().next().getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(VideoFavorite favorite) {
            assertThat(favorite.getUser().getUsername(), is(equalTo("User 1")));
            assertThat(favorite.getVideo(), is(not(nullValue())));
            assertThat(favorite.getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(VideoFavorite favorite) {
            assertThat(favorite.getUser().getUsername(), is(equalTo("User 1")));
            assertThat(favorite.getVideo(), is(nullValue()));
        }
    }

    @Nested
    class VideoVideoRatingTest extends GenericEntityTester<Video, VideoRating> {

        @Autowired
        private UserRepository userRepo;

        @Autowired
        public VideoVideoRatingTest(VideoRepository videoRepo, VideoRatingRepository ratingRepo) {
            super("video", "rating", videoRepo, ratingRepo);
        }

        @Transactional
        @Scenario("Persisting a video will persist attached ratings")
        void testPersistVideoAndAttachedRating() {
            testPersistEntityAndAttachedEntities();
        }

        @Transactional
        @Scenario("When deleting a video its ratings are deleted as well")
        void testVideoDeletionRemovesRating() {
            testEntityDeletionRemovesAttachedEntities();
        }

        @Override
        public Video createEntity() {
            return Video.builder().title("Video 1").build();
        }

        @Override
        public VideoRating createAttachedEntity() {
            User user = User.builder().username("User 1").password("pass1").build();
            user = userRepo.save(user);
            return VideoRating.builder().rating(3).user(user).build();
        }

        @Override
        public void attachToEntity(Video video, VideoRating rating) {
            video.addRating(rating);
        }

        @Override
        public void assertEntityHasAttachedEntity(Video video) {
            assertThat(video.getTitle(), is(equalTo("Video 1")));
            assertThat(video.getRatings(), hasSize(1));
            assertThat(video.getRatings().iterator().next().getRating(), is(equalTo(3)));
            assertThat(video.getRatings().iterator().next().getUser().getUsername(), is(equalTo("User 1")));
        }

        @Override
        public void assertAttachedEntityHasEntityAttached(VideoRating rating) {
            assertThat(rating.getRating(), is(equalTo(3)));
            assertThat(rating.getUser().getUsername(), is(equalTo("User 1")));
            assertThat(rating.getVideo(), is(not(nullValue())));
            assertThat(rating.getVideo().getTitle(), is(equalTo("Video 1")));
        }

        @Override
        public void assertAttachedEntityHasNothingAttached(VideoRating rating) {
            assertThat(rating.getRating(), is(equalTo(3)));
            assertThat(rating.getUser().getUsername(), is(equalTo("User 1")));
            assertThat(rating.getVideo(), is(nullValue()));
        }
    }
}
