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
package de.gmasil.webproject.jpa.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.role.Role;
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
@Entity(name = "USER")
@Table(name = "USER")
public class User extends Auditable implements UserDetails {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

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

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
