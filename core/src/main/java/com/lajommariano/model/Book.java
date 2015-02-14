package com.lajommariano.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="book")
public class Book extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private Timestamp publishDate;
	private String synopsis;
	private boolean isPremium;
	private Writer writer;
	private List<Chapter> chapters = new ArrayList<Chapter>();
	private List<Reader> subscribers = new ArrayList<Reader>();
	private List<Genre> genres = new ArrayList<Genre>();
	private List<Tag> tags = new ArrayList<Tag>();

	@Column(name="title", nullable=false, unique=true, length=200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="publish_date", nullable=true)
	public Timestamp getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Timestamp publishDate) {
		this.publishDate = publishDate;
	}

	@Column(name="synopsis", nullable=false)
	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	@Column(name="is_premium", nullable=false)
	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="user_id", nullable=false)
	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="book")
	public List<Chapter> getChapters() {
		return chapters;
	}
	
	@ManyToMany(fetch=FetchType.LAZY, mappedBy="subscriptions")
	public List<Reader> getSubscribers() {
		return subscribers;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinTable(
		name = "Book_Genre_Assign", 
		joinColumns = { @JoinColumn(name="book_id", nullable=false, updatable=false) }, 
		inverseJoinColumns = { @JoinColumn(name = "genre_id", nullable=false, updatable=false) }
	)
	public List<Genre> getGenres() {
		return genres;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="book")
	public List<Tag> getTags() {
		return tags;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public void setSubscribers(List<Reader> subscribers) {
		this.subscribers = subscribers;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}
