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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.gmasil.webproject.jpa.PersistenceObject;
import de.gmasil.webproject.jpa.comment.Comment;
import de.gmasil.webproject.jpa.role.RoleDAO;
import de.gmasil.webproject.jpa.videorating.VideoRatingDAO;

@Entity(name = "USER")
@Table(name = "USER")
public class UserDAO extends PersistenceObject implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<RoleDAO> roles = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<VideoRatingDAO> ratings = new LinkedList<>();

    @Generated("SparkTools")
    private UserDAO(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.id = builder.id;
        this.created = builder.created;
        this.roles = builder.roles;
        this.comments = builder.comments;
        this.ratings = builder.ratings;
        for (Comment comment : this.comments) {
            comment.setUser(this);
        }
        for (VideoRatingDAO rating : this.ratings) {
            rating.setUser(this);
        }
    }

    public UserDAO() {
    }

    public UserDAO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleDAO> getRoles() {
        return roles;
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
     * Creates builder to build {@link UserDAO}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static IUsernameStage builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public interface IUsernameStage {

        public IPasswordStage withUsername(String username);
    }

    @Generated("SparkTools")
    public interface IPasswordStage {

        public IBuildStage withPassword(String password);
    }

    @Generated("SparkTools")
    public interface IBuildStage {

        public IBuildStage withId(Long id);

        public IBuildStage withCreated(LocalDateTime created);

        public IBuildStage withRoles(List<RoleDAO> roles);

        public IBuildStage withComments(List<Comment> comments);

        public IBuildStage withRatings(List<VideoRatingDAO> ratings);

        public UserDAO build();
    }

    /**
     * Builder to build {@link UserDAO}.
     */
    @Generated("SparkTools")
    public static final class Builder implements IUsernameStage, IPasswordStage, IBuildStage {

        private String username;
        private String password;
        private Long id;
        private LocalDateTime created;
        private List<RoleDAO> roles = new LinkedList<>();
        private List<Comment> comments = new LinkedList<>();
        private List<VideoRatingDAO> ratings = new LinkedList<>();

        private Builder() {
        }

        @Override
        public IPasswordStage withUsername(String username) {
            this.username = username;
            return this;
        }

        @Override
        public IBuildStage withPassword(String password) {
            this.password = password;
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
        public IBuildStage withRoles(List<RoleDAO> roles) {
            this.roles = roles;
            return this;
        }

        @Override
        public IBuildStage withComments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        @Override
        public IBuildStage withRatings(List<VideoRatingDAO> ratings) {
            this.ratings = ratings;
            return this;
        }

        @Override
        public UserDAO build() {
            return new UserDAO(this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
}
