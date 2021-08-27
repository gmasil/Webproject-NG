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
package de.gmasil.webproject.jpa.artist;

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

@Entity(name = "ARTIST")
@Table(name = "ARTIST")
public class ArtistDAO extends PersistenceObject {
	@Column(nullable = false)
	private String name;

	private String picfile;

	@ManyToMany(mappedBy = "artists", cascade = CascadeType.ALL)
	private List<VideoDAO> videos = new LinkedList<>();

	@Generated("SparkTools")
	private ArtistDAO(Builder builder) {
		this.name = builder.name;
		this.id = builder.id;
		this.created = builder.created;
		this.picfile = builder.picfile;
		this.videos = builder.videos;
	}

	public ArtistDAO() {
	}

	public ArtistDAO(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicfile() {
		return picfile;
	}

	public void setPicfile(String picfile) {
		this.picfile = picfile;
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
	 * Creates builder to build {@link ArtistDAO}.
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

		public IBuildStage withPicfile(String picfile);

		public IBuildStage withVideos(List<VideoDAO> videos);

		public ArtistDAO build();
	}

	/**
	 * Builder to build {@link ArtistDAO}.
	 */
	@Generated("SparkTools")
	public static final class Builder implements INameStage, IBuildStage {
		private String name;
		private Long id;
		private LocalDateTime created;
		private String picfile;
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
		public IBuildStage withPicfile(String picfile) {
			this.picfile = picfile;
			return this;
		}

		@Override
		public IBuildStage withVideos(List<VideoDAO> videos) {
			this.videos = videos;
			return this;
		}

		@Override
		public ArtistDAO build() {
			return new ArtistDAO(this);
		}
	}
}
