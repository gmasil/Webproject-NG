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
package de.gmasil.webproject.jpa.scene;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import de.gmasil.webproject.dto.SceneDto;
import de.gmasil.webproject.dto.SceneDto.SceneDtoBuilder;
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
@Entity(name = "SCENE")
@Table(name = "SCENE")
@EntityListeners(AuditingEntityListener.class)
public class Scene extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float time;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "VIDEO_ID")
    private Video video;

    @Builder
    public Scene(String name, float time) {
        this.name = name;
        this.time = time;
    }

    public SceneDto toDto() {
        SceneDtoBuilder builder = SceneDto.builder();
        builder.id(getId());
        builder.name(getName());
        builder.time(getTime());
        return builder.build();
    }

    @PreRemove
    private void preRemove() {
        video.getScenes().remove(this);
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