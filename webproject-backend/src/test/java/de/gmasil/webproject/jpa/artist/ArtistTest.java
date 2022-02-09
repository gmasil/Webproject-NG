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
package de.gmasil.webproject.jpa.artist;

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
class ArtistTest extends GenericEntityTester<Artist, Video> {

    @Autowired
    public ArtistTest(ArtistRepository artistRepo, VideoRepository videoRepo) {
        super("artist", "video", artistRepo, videoRepo);
    }

    @Transactional
    @Scenario("Persisting an artist will persist attached videos")
    void testPersistArtistAndAttachedVideos() {
        testPersistEntityAndAttachedEntities();
    }

    @Transactional
    @Scenario("When deleting an artist it is detached from a video while preserving it")
    void testArtistDeletionPreservesVideos() {
        testEntityDeletionPreservesAttachedEntities();
    }

    @Override
    public Artist createEntity() {
        return Artist.builder().name("Artist 1").build();
    }

    @Override
    public Video createAttachedEntity() {
        return Video.builder().title("Video 1").build();
    }

    @Override
    public void attachToEntity(Artist artist, Video video) {
        artist.addVideo(video);
    }

    @Override
    public void assertEntityHasAttachedEntity(Artist artist) {
        assertThat(artist.getName(), is(equalTo("Artist 1")));
        assertThat(artist.getVideos(), hasSize(1));
        assertThat(artist.getVideos().iterator().next().getTitle(), is(equalTo("Video 1")));
    }

    @Override
    public void assertAttachedEntityHasEntityAttached(Video video) {
        assertThat(video.getTitle(), is(equalTo("Video 1")));
        assertThat(video.getArtists(), hasSize(1));
        assertThat(video.getArtists().iterator().next().getName(), is(equalTo("Artist 1")));
    }

    @Override
    public void assertAttachedEntityHasNothingAttached(Video video) {
        assertThat(video.getTitle(), is(equalTo("Video 1")));
        assertThat(video.getArtists(), hasSize(0));
    }
}
