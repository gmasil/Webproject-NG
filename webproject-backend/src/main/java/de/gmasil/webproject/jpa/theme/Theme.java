/*
 * Webproject NG
 * Copyright © 2022 Gmasil
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
package de.gmasil.webproject.jpa.theme;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.gmasil.webproject.jpa.Auditable;
import de.gmasil.webproject.jpa.ColorConverter;
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
@Entity(name = "THEME")
@Table(name = "THEME", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "USER_ID" }) })
@EntityListeners(AuditingEntityListener.class)
public class Theme extends Auditable {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "USER_ID", nullable = true)
    private User creator;

    @Column(nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color backgroundColor;

    @Column(nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color backgroundHighlightColor;

    @Column(nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color primaryColor;

    @Column(nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color secondaryColor;

    @Column(nullable = false)
    @Convert(converter = ColorConverter.class)
    private Color textColor;

    private boolean preset = false;

    @OneToMany(mappedBy = "activeTheme", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private Set<User> activeBy = new HashSet<>();

    @Builder
    public Theme(String name, User creator, Color backgroundColor, Color backgroundHighlightColor, Color primaryColor,
            Color secondaryColor, Color textColor, boolean preset) {
        this.name = name;
        setCreator(creator);
        this.backgroundColor = backgroundColor;
        this.backgroundHighlightColor = backgroundHighlightColor;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.textColor = textColor;
        this.preset = preset;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        creator.getCreatedThemes().add(this);
    }

    @PreRemove
    private void preRemove() {
        for (User user : activeBy) {
            user.setActiveTheme(null);
        }
    }

    public String toCss(ColorConverter colorConverter) {
        String template = ":root{" //
                + "--theme-background:%s;" //
                + "--theme-background-highlight:%s;" //
                + "--theme-primary:%s;" //
                + "--theme-secondary:%s;" //
                + "--theme-text:%s" //
                + "}";
        return String.format(template, //
                colorConverter.convertToDatabaseColumn(backgroundColor), //
                colorConverter.convertToDatabaseColumn(backgroundHighlightColor), //
                colorConverter.convertToDatabaseColumn(primaryColor), //
                colorConverter.convertToDatabaseColumn(secondaryColor), //
                colorConverter.convertToDatabaseColumn(textColor));
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
