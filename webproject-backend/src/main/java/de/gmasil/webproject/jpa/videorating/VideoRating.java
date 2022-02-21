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
package de.gmasil.webproject.jpa.videorating;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
@Entity(name = "VIDEO_RATING")
@Table(name = "VIDEO_RATING")
@EntityListeners(AuditingEntityListener.class)
public class VideoRating extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private int rating;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "VIDEO_ID", nullable = false)
    private Video video;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public VideoRating(int rating, Video video, User user) {
        setRating(rating);
        setVideo(video);
        setUser(user);
    }

    public void setVideo(Video video) {
        this.video = video;
        if (video != null) {
            video.getRatings().add(this);
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.getRatings().add(this);
        }
    }

    @PreRemove
    private void preRemove() {
        if (video != null) {
            video.getRatings().remove(this);
        }
        if (user != null) {
            user.getRatings().remove(this);
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
