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
package de.gmasil.webproject.jpa.videofile;

import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.gmasil.webproject.jpa.PersistenceObject;
import de.gmasil.webproject.jpa.VideoQuality;

@Entity(name = "FILE")
@Table(name = "FILE", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "quality" }) })
public class VideoFileDAO extends PersistenceObject {

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private VideoQuality quality;

    @Generated("SparkTools")
    private VideoFileDAO(Builder builder) {
        this.name = builder.name;
        this.quality = builder.quality;
        this.id = builder.id;
        this.created = builder.created;
    }

    public VideoFileDAO() {
    }

    public VideoFileDAO(String name, VideoQuality quality) {
        this.name = name;
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VideoQuality getQuality() {
        return quality;
    }

    public void setQuality(VideoQuality quality) {
        this.quality = quality;
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
     * Creates builder to build {@link VideoFileDAO}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static INameStage builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public interface INameStage {

        public IQualityStage withName(String name);
    }

    @Generated("SparkTools")
    public interface IQualityStage {

        public IBuildStage withQuality(VideoQuality quality);
    }

    @Generated("SparkTools")
    public interface IBuildStage {

        public IBuildStage withId(Long id);

        public IBuildStage withCreated(LocalDateTime created);

        public VideoFileDAO build();
    }

    /**
     * Builder to build {@link VideoFileDAO}.
     */
    @Generated("SparkTools")
    public static final class Builder implements INameStage, IQualityStage, IBuildStage {

        private String name;
        private VideoQuality quality;
        private Long id;
        private LocalDateTime created;

        private Builder() {
        }

        @Override
        public IQualityStage withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public IBuildStage withQuality(VideoQuality quality) {
            this.quality = quality;
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
        public VideoFileDAO build() {
            return new VideoFileDAO(this);
        }
    }
}
