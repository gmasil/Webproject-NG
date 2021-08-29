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

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.artist.Artist;
import de.gmasil.webproject.jpa.category.Category;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.videofile.VideoFileDAO;
import de.gmasil.webproject.jpa.videorating.VideoRatingDAO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "VIDEO")
@Table(name = "VIDEO")
public class Video extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private float length;

    private String thumbnail;

    @ManyToMany
    @JoinTable(name = "VIDEO_FILES", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "FILE_ID"))
    private Set<VideoFileDAO> files;

    @ManyToMany
    @JoinTable(name = "VIDEO_ARTISTS", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    private Set<Artist> artists;

    @ManyToMany
    @JoinTable(name = "VIDEO_CATEGORIES", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private Set<Category> categories;

    @OneToMany(mappedBy = "video")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "video")
    private Set<VideoRatingDAO> ratings;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
