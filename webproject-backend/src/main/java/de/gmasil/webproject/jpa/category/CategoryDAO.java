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
package de.gmasil.webproject.jpa.category;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.gmasil.webproject.jpa.PersistenceObject;
import de.gmasil.webproject.jpa.video.VideoDAO;

@Entity(name = "CATEGORY")
@Table(name = "CATEGORY")
public class CategoryDAO extends PersistenceObject {
	@Column(nullable = false, unique = true)
	private String name;

	@ManyToMany(mappedBy = "categories", cascade = CascadeType.ALL)
	private List<VideoDAO> videos = new LinkedList<>();

	@Generated("SparkTools")
	private CategoryDAO(Builder builder) {
		this.name = builder.name;
		this.id = builder.id;
		this.created = builder.created;
		this.videos = builder.videos;
	}

	public CategoryDAO() {
	}

	public CategoryDAO(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VideoDAO> getVideos() {
		return videos;
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
	 * Creates builder to build {@link CategoryDAO}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static INameStage builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public interface INameStage {
		public IBuildStage withName(String name);
	}

	@Generated("SparkTools")
	public interface IBuildStage {
		public IBuildStage withId(Long id);

		public IBuildStage withCreated(LocalDateTime created);

		public IBuildStage withVideos(List<VideoDAO> videos);

		public CategoryDAO build();
	}

	/**
	 * Builder to build {@link CategoryDAO}.
	 */
	@Generated("SparkTools")
	public static final class Builder implements INameStage, IBuildStage {
		private String name;
		private Long id;
		private LocalDateTime created;
		private List<VideoDAO> videos = new LinkedList<>();

		private Builder() {
		}

		@Override
		public IBuildStage withName(String name) {
			this.name = name;
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
		public IBuildStage withVideos(List<VideoDAO> videos) {
			this.videos = videos;
			return this;
		}

		@Override
		public CategoryDAO build() {
			return new CategoryDAO(this);
		}
	}
}
