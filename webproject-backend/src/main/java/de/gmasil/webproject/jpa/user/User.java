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
package de.gmasil.webproject.jpa.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.gmasil.webproject.dto.UserDto;
import de.gmasil.webproject.dto.UserDto.UserDtoBuilder;
import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.role.Role;
import de.gmasil.webproject.jpa.theme.Theme;
import de.gmasil.webproject.jpa.videofavorite.VideoFavorite;
import de.gmasil.webproject.jpa.videorating.VideoRating;
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
@Entity(name = "W_USER")
@Table(name = "W_USER")
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable implements UserDetails {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = { CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REMOVE })
    private Set<VideoFavorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<VideoRating> ratings = new HashSet<>();

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "THEME_ID", nullable = true)
    private Theme activeTheme = null;

    @OneToMany(mappedBy = "creator", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<Theme> createdThemes = new HashSet<>();

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto toDto() {
        UserDtoBuilder builder = UserDto.builder();
        builder.id(getId());
        builder.username(getUsername());
        builder.roles(getRoles().stream().map(Role::toDto).collect(Collectors.toSet()));
        if (getActiveTheme() != null) {
            builder.activeTheme(getActiveTheme().toDto());
        }
        return builder.build();
    }

    public void addRole(Role role) {
        roles.add(role);
        role.addUser(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void addFavorite(VideoFavorite favorite) {
        favorites.add(favorite);
        favorite.setUser(this);
    }

    public void addRating(VideoRating rating) {
        ratings.add(rating);
        rating.setUser(this);
    }

    public void setActiveTheme(Theme theme) {
        if (activeTheme != null) {
            activeTheme.getActiveBy().remove(this);
        }
        activeTheme = theme;
        if (theme != null) {
            theme.getActiveBy().add(this);
        }
    }

    public void addCreatedTheme(Theme theme) {
        createdThemes.add(theme);
        theme.setCreator(this);
    }

    public boolean hasRole(String role) {
        return getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(name -> name.equals(role))
                .count() > 0;
    }

    @PreRemove
    private void preRemove() {
        for (Role role : roles) {
            role.getUsers().remove(this);
        }
        for (Comment comment : comments) {
            comment.setUser(null);
        }
        // do not detach favorites, delete them
        for (VideoRating rating : ratings) {
            rating.setUser(null);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
