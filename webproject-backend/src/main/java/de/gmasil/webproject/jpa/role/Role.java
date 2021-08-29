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
package de.gmasil.webproject.jpa.role;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.user.User;
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
@Entity(name = "ROLE")
@Table(name = "ROLE")
public class Role extends Auditable implements GrantedAuthority {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<User> users = new HashSet<>();

    @Override
    public String getAuthority() {
        return getName();
    }

    @Builder
    public Role(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        users.add(user);
        user.getRoles().add(this);
    }

    @PreRemove
    private void preRemove() {
        for (User user : users) {
            user.getRoles().remove(this);
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
