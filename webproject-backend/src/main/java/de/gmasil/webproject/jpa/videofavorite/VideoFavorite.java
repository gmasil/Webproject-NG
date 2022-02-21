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
package de.gmasil.webproject.jpa.videofavorite;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.video.Video;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "VIDEO_FAVORITES")
@Table(name = "VIDEO_FAVORITES")
@EntityListeners(AuditingEntityListener.class)
public class VideoFavorite extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "VIDEO_ID", nullable = false)
    private Video video;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Builder
    public VideoFavorite(Video video, User user) {
        setVideo(video);
        setUser(user);
    }

    public void setVideo(Video video) {
        this.video = video;
        if (video != null) {
            video.getFavorites().add(this);
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.getFavorites().add(this);
        }
    }

    @PreRemove
    private void preRemove() {
        if (video != null) {
            video.getFavorites().remove(this);
        }
        if (user != null) {
            user.getFavorites().remove(this);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
