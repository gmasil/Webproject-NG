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
package de.gmasil.webproject.service.dataimport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.gmasil.webproject.jpa.artist.ArtistRepository;
import de.gmasil.webproject.jpa.category.CategoryRepository;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.UserRepository;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videofile.VideoFileRepository;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.utils.ImportTestData;
import de.gmasil.webproject.utils.SetupTestContext;

@ImportTestData
@SetupTestContext
class DataImportServiceTest {

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

    @Test
    void testForExistingData() throws IOException {
        assertThat(artistRepo.count(), is(equalTo(3L)));
        assertThat(categoryRepo.count(), is(equalTo(3L)));
        assertThat(commentRepo.count(), is(equalTo(2L)));
        assertThat(propertyRepo.count(), is(equalTo(1L)));
        assertThat(roleRepo.count(), is(equalTo(3L)));
        assertThat(themeRepo.count(), is(equalTo(3L)));
        assertThat(userRepo.count(), is(equalTo(2L)));
        assertThat(videoRepo.count(), is(equalTo(2L)));
        assertThat(favoriteRepo.count(), is(equalTo(2L)));
        assertThat(fileRepo.count(), is(equalTo(3L)));
        assertThat(ratingRepo.count(), is(equalTo(2L)));
    }
}
