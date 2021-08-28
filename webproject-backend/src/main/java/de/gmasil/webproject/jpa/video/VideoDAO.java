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
package de.gmasil.webproject.jpa.video;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.gmasil.webproject.jpa.PersistenceObject;
import de.gmasil.webproject.jpa.artist.Artist;
import de.gmasil.webproject.jpa.category.Category;
import de.gmasil.webproject.jpa.comment.CommentDAO;
import de.gmasil.webproject.jpa.videofile.VideoFileDAO;
import de.gmasil.webproject.jpa.videorating.VideoRatingDAO;

@Entity(name = "VIDEO")
@Table(name = "VIDEO")
public class VideoDAO extends PersistenceObject {

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private float length;

    private String thumbnail;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "VIDEO_FILES", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "FILE_ID"))
    private List<VideoFileDAO> files = new LinkedList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "VIDEO_ARTISTS", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    private List<Artist> artists = new LinkedList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "VIDEO_CATEGORIES", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private List<Category> categories = new LinkedList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<CommentDAO> comments = new LinkedList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<VideoRatingDAO> ratings = new LinkedList<>();

    @Generated("SparkTools")
    private VideoDAO(Builder builder) {
        this.title = builder.title;
        this.id = builder.id;
        this.created = builder.created;
        this.description = builder.description;
        this.length = builder.length;
        this.thumbnail = builder.thumbnail;
        this.files = builder.files;
        this.artists = builder.artists;
        this.categories = builder.categories;
        this.comments = builder.comments;
        this.ratings = builder.ratings;
        for (CommentDAO comment : this.comments) {
            comment.setVideo(this);
        }
        for (VideoRatingDAO rating : this.ratings) {
            rating.setVideo(this);
        }
    }

    public VideoDAO() {
    }

    public VideoDAO(String title, String description, float length, String thumbnail) {
        this.title = title;
        this.description = description;
        this.length = length;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<VideoFileDAO> getFiles() {
        return files;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<CommentDAO> getComments() {
        return comments;
    }

    public List<VideoRatingDAO> getRatings() {
        return ratings;
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
     * Creates builder to build {@link VideoDAO}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static ITitleStage builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public interface ITitleStage {

        public IBuildStage withTitle(String title);
    }

    @Generated("SparkTools")
    public interface IBuildStage {

        public IBuildStage withId(Long id);

        public IBuildStage withCreated(LocalDateTime created);

        public IBuildStage withDescription(String description);

        public IBuildStage withLength(float length);

        public IBuildStage withThumbnail(String thumbnail);

        public IBuildStage withFiles(List<VideoFileDAO> files);

        public IBuildStage withArtists(List<Artist> artists);

        public IBuildStage withCategories(List<Category> categories);

        public IBuildStage withComments(List<CommentDAO> comments);

        public IBuildStage withRatings(List<VideoRatingDAO> ratings);

        public VideoDAO build();
    }

    /**
     * Builder to build {@link VideoDAO}.
     */
    @Generated("SparkTools")
    public static final class Builder implements ITitleStage, IBuildStage {

        private String title;
        private Long id;
        private LocalDateTime created;
        private String description;
        private float length;
        private String thumbnail;
        private List<VideoFileDAO> files = new LinkedList<>();
        private List<Artist> artists = new LinkedList<>();
        private List<Category> categories = new LinkedList<>();
        private List<CommentDAO> comments = new LinkedList<>();
        private List<VideoRatingDAO> ratings = new LinkedList<>();

        private Builder() {
        }

        @Override
        public IBuildStage withTitle(String title) {
            this.title = title;
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
        public IBuildStage withDescription(String description) {
            this.description = description;
            return this;
        }

        @Override
        public IBuildStage withLength(float length) {
            this.length = length;
            return this;
        }

        @Override
        public IBuildStage withThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        @Override
        public IBuildStage withFiles(List<VideoFileDAO> files) {
            this.files = files;
            return this;
        }

        @Override
        public IBuildStage withArtists(List<Artist> artists) {
            this.artists = artists;
            return this;
        }

        @Override
        public IBuildStage withCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        @Override
        public IBuildStage withComments(List<CommentDAO> comments) {
            this.comments = comments;
            return this;
        }

        @Override
        public IBuildStage withRatings(List<VideoRatingDAO> ratings) {
            this.ratings = ratings;
            return this;
        }

        @Override
        public VideoDAO build() {
            return new VideoDAO(this);
        }
    }
}
