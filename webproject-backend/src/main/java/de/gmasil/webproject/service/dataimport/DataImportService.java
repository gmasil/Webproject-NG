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

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.gmasil.webproject.jpa.artist.ArtistRepository;
import de.gmasil.webproject.jpa.category.CategoryRepository;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.comment.CommentRepository;
import de.gmasil.webproject.jpa.globalproperty.Property;
import de.gmasil.webproject.jpa.globalproperty.PropertyRepository;
import de.gmasil.webproject.jpa.role.RoleRepository;
import de.gmasil.webproject.jpa.scene.Scene;
import de.gmasil.webproject.jpa.scene.SceneRepository;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.theme.ThemeRepository;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.user.UserService;
import de.gmasil.webproject.jpa.video.Video;
import de.gmasil.webproject.jpa.video.VideoRepository;
import de.gmasil.webproject.jpa.videofavorite.VideoFavorite;
import de.gmasil.webproject.jpa.videofavorite.VideoFavoriteRepository;
import de.gmasil.webproject.jpa.videofile.VideoFile;
import de.gmasil.webproject.jpa.videorating.VideoRating;
import de.gmasil.webproject.jpa.videorating.VideoRatingRepository;
import de.gmasil.webproject.service.dataimport.ImportData.ImportTheme;
import de.gmasil.webproject.service.dataimport.ImportData.ImportUser;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportComment;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportFile;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportRating;
import de.gmasil.webproject.service.dataimport.ImportData.ImportVideo.ImportScene;
import de.gmasil.webproject.service.initialize.InitializeFinishedEvent;

@Service
public class DataImportService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String UNREFERENCED_USER = "User '%s' is referenced in video '%s', but was not declared in users section!";

    @Autowired
    private DataImportProperties properties;

    @Autowired
    private Module module;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
    private VideoRepository videoRepo;

    @Autowired
    private VideoFavoriteRepository favoriteRepo;

    @Autowired
    private VideoRatingRepository ratingRepo;

    @Autowired
    private SceneRepository sceneRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void clean() {
        if (properties.isEnabled() && properties.isClean()) {
            performClean();
        }
        eventPublisher.publishEvent(new CleanFinishedEvent(this));
    }

    @Transactional
    public void performClean() {
        List<JpaRepository<?, ?>> repositories = ctx.getBeansOfType(JpaRepository.class).values().stream()
                .map(s -> (JpaRepository<?, ?>) s).collect(Collectors.toList());
        LOG.info("Cleaning {} repositories", repositories.size());
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        repositories.forEach(JpaRepository::deleteAllInBatch);
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    @EventListener(InitializeFinishedEvent.class)
    public void importData() throws IOException {
        if (properties.isEnabled()) {
            performDataImport();
        }
        eventPublisher.publishEvent(new DataImportFinishedEvent(this));
    }

    @Transactional
    public void performDataImport() throws IOException {
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

    private void importData(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(module);
        ImportData data = objectMapper.readValue(file, ImportData.class);
        importThemes(data);
        importUsers(data);
        importVideos(data);
    }

    private void importThemes(ImportData data) {
        for (ImportTheme t : data.getThemes()) {
            Theme theme = new Theme();
            mapper.map(t, theme);
            theme.setPreset(true);
            theme = themeRepo.save(theme);
            if (t.isDefault()) {
                propertyRepo.setProperty(Property.DEFAULT_THEME, "" + theme.getId());
            }
        }
    }

    private void importUsers(ImportData data) {
        for (ImportUser u : data.getUsers()) {
            User user = User.builder() //
                    .username(u.getUsername()) //
                    .password(u.getPassword()) //
                    .build();
            userService.encodePassword(user);
            for (ImportTheme t : u.getThemes()) {
                Theme theme = new Theme();
                mapper.map(t, theme);
                theme.setPreset(false);
                theme.setCreator(user);
            }
            for (String r : u.getRoles()) {
                user.addRole(roleRepo.findByNameOrCreate(r));
            }
            user = userService.save(user);
            if (u.getActiveTheme() != null) {
                Theme userActiveTheme = themeRepo.findAllAvailable(user).stream()
                        .filter(t -> t.getName().equals(u.getActiveTheme())).findAny()
                        .orElseThrow(() -> new IllegalStateException(
                                "The theme with id " + u.getActiveTheme() + " does not exist."));
                user.setActiveTheme(userActiveTheme);
            }
            userService.save(user);
        }
    }

    private void importVideos(ImportData data) {
        for (ImportVideo v : data.getVideos()) {
            Video video = Video.builder() //
                    .title(v.getTitle()) //
                    .description(v.getDescription()) //
                    .length(v.getLength()) //
                    .thumbnail(data.getPaths().getPrefix().getThumbnail() + v.getThumbnail()) //
                    .build();
            for (ImportFile file : v.getFiles()) {
                video.addFile(VideoFile.builder().name(data.getPaths().getPrefix().getVideo() + file.getName())
                        .quality(file.getQuality()).build());
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
            // Scene, VideoFavorite and VideoRating require the video to exist first
            for (ImportScene scene : v.getScenes()) {
                importScene(video, scene);
            }
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

    private void importScene(Video video, ImportScene scene) {
        Scene is = Scene.builder().name(scene.getName()).time(scene.getTime()).build();
        is.setVideo(video);
        sceneRepo.save(is);
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
}
