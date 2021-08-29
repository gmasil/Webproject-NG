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
package de.gmasil.webproject.jpa.videofavorite;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

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
public class VideoFavorite extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "VIDEO_ID", nullable = false)
    private Video video;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public VideoFavorite(Video video, User user) {
        this.video = video;
        this.user = user;
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
