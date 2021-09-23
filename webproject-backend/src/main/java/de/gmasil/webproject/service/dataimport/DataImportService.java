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

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.artist.ArtistRepository;
import de.gmasil.webproject.jpa.category.CategoryRepository;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavorite;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videofile.VideoFile;
import de.gmasil.webproject.jpa.videorating.VideoRating;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.service.dataimport.ImportData.ImportUser;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportComment;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportFile;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportRating;
import de.gmasil.webproject.service.initialize.InitializeFinishedEvent;

@Service
public class DataImportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String UNREFERENCED_USER = "User '%s' is referenced in video '%s', but was not declared in users section!";

    @Autowired
    private DataImportProperties properties;

    @Autowired
    private List<JpaRepository<? extends Auditable, Long>> repositories;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private VideoRepository videoRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private VideoFavoriteRepository favoriteRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private VideoRatingRepository ratingRepo;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener(ApplicationReadyEvent.class)
    public void clean() {
        if (properties.isEnabled() && properties.isClean()) {
            LOG.info("Cleaning existing data");
            deleteAllData();
        }
        eventPublisher.publishEvent(new CleanFinishedEvent(this));
    }

    @Transactional
    @EventListener(InitializeFinishedEvent.class)
    public void importData() throws IOException {
        if (properties.isEnabled()) {
            File file = new File(properties.getFile());
            if (file.exists()) {
                StopWatch watch = new StopWatch();
                watch.start();
                LOG.info("Importing data from {}", file.getAbsolutePath());
                importData(file);
                watch.stop();
                LOG.info("Data import finished in {}s", watch.getTotalTimeSeconds());
            } else {
                LOG.warn("Cannot import data, file does not exist: {}", file.getAbsolutePath());
            }
        }
        eventPublisher.publishEvent(new DataImportFinishedEvent(this));
    }

    private void importData(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        ImportData data = mapper.readValue(file, ImportData.class);
        importUsers(data);
        importVideos(data);
    }

    private void importUsers(ImportData data) {
        for (ImportUser u : data.getUsers()) {
            User user = User.builder() //
                    .username(u.getUsername()) //
                    .password(u.getPassword()) //
                    .build();
            for (String r : u.getRoles()) {
                user.addRole(roleRepo.findByNameOrCreate(r));
            }
            userService.encodePassword(user);
            userService.save(user);
        }
    }

    private void importVideos(ImportData data) {
        for (ImportVideo v : data.getVideos()) {
            Video video = Video.builder() //
                    .title(v.getTitle()) //
                    .description(v.getDescription()) //
                    .length(v.getLength()) //
                    .thumbnail(v.getThumbnail()) //
                    .build();
            for (ImportFile file : v.getFiles()) {
                video.addFile(VideoFile.builder().name(file.getName()).quality(file.getQuality()).build());
            }
            for (String artist : v.getArtists()) {
                video.addArtist(artistRepo.findByNameOrCreate(artist));
            }
            for (String category : v.getCategories()) {
                video.addCategory(categoryRepo.findByNameOrCreate(category));
            }
            for (ImportComment comment : v.getComments()) {
                importVideoComment(video, comment);
            }
            video = videoRepo.save(video);
            // VideoFavorite and VideoRating require the video to exist first
            for (String favoriter : v.getFavoriters()) {
                importVideoFavorites(video, favoriter);
            }
            for (ImportRating rating : v.getRatings()) {
                importVideoRating(video, rating);
            }
        }
    }

    private void importVideoComment(Video video, ImportComment importComment) {
        Comment comment = Comment.builder().text(importComment.getComment()).build();
        User user = getUserByUsername(importComment.getUsername(), video);
        comment.setUser(user);
        comment.setVideo(video);
        commentRepo.save(comment);
    }

    private void importVideoFavorites(Video video, String favoriter) {
        User user = getUserByUsername(favoriter, video);
        VideoFavorite favorite = new VideoFavorite();
        favorite.setUser(user);
        favorite.setVideo(video);
        favoriteRepo.save(favorite);
    }

    private void importVideoRating(Video video, ImportRating importRating) {
        User user = getUserByUsername(importRating.getUsername(), video);
        VideoRating rating = VideoRating.builder().rating(importRating.getRating()).build();
        rating.setUser(user);
        rating.setVideo(video);
        ratingRepo.save(rating);
    }

    private User getUserByUsername(String username, Video video) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new IllegalStateException(String.format(UNREFERENCED_USER, username, video.getTitle()));
        }
        return optionalUser.get();
    }

    private void deleteAllData() {
        repositories.forEach(JpaRepository::deleteAll);
    }
}
