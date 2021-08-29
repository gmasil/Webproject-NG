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

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import de.gmasil.webproject.jpa.PersistenceObject;
import de.gmasil.webproject.jpa.user.User;
import de.gmasil.webproject.jpa.video.Video;

@Entity(name = "VIDEO_RATING")
@Table(name = "VIDEO_RATING")
public class VideoRatingDAO extends PersistenceObject {

    @Min(1)
    @Max(5)
    private int rating;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "VIDEO_ID")
    private Video video;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Generated("SparkTools")
    private VideoRatingDAO(Builder builder) {
        this.rating = builder.rating;
        this.id = builder.id;
        this.created = builder.created;
        this.video = builder.video;
        this.user = builder.user;
    }

    public VideoRatingDAO() {
    }

    public VideoRatingDAO(Video video, User user, int rating) {
        this.video = video;
        this.user = user;
        this.rating = rating;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Creates builder to build {@link VideoRatingDAO}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static IRatingStage builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public interface IRatingStage {

        public IBuildStage withRating(int rating);
    }

    @Generated("SparkTools")
    public interface IBuildStage {

        public IBuildStage withId(Long id);

        public IBuildStage withCreated(LocalDateTime created);

        public IBuildStage withVideo(Video video);

        public IBuildStage withUser(User user);

        public VideoRatingDAO build();
    }

    /**
     * Builder to build {@link VideoRatingDAO}.
     */
    @Generated("SparkTools")
    public static final class Builder implements IRatingStage, IBuildStage {

        private int rating;
        private Long id;
        private LocalDateTime created;
        private Video video;
        private User user;

        private Builder() {
        }

        @Override
        public IBuildStage withRating(int rating) {
            this.rating = rating;
            return this;
        }

        @Override
        public IBuildStage withId(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public IBuildStage withCreated(LocalDateTime created) {
            this.created = created;
            return this;
        }

        @Override
        public IBuildStage withVideo(Video video) {
            this.video = video;
            return this;
        }

        @Override
        public IBuildStage withUser(User user) {
            this.user = user;
            return this;
        }

        @Override
        public VideoRatingDAO build() {
            return new VideoRatingDAO(this);
        }
    }
}
