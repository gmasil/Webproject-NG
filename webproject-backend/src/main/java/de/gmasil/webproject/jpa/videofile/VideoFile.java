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
package de.gmasil.webproject.jpa.videofile;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.gmasil.webproject.jpa.Auditable;
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
@Entity(name = "FILE")
@Table(name = "FILE", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "quality" }) })
@EntityListeners(AuditingEntityListener.class)
public class VideoFile extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String quality;

    @ManyToMany(mappedBy = "files", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<Video> videos = new HashSet<>();

    @Builder
    public VideoFile(String name, String quality) {
        this.name = name;
        this.quality = quality;
    }

    public void addVideo(Video video) {
        videos.add(video);
        video.getFiles().add(this);
    }

    @PreRemove
    private void preRemove() {
        for (Video video : videos) {
            video.getFiles().remove(this);
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