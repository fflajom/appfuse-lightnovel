package com.lajommariano.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name="Chapter", 
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "title", "book_id" }),
		@UniqueConstraint(columnNames = { "number", "book_id" }) 
	}
)
public class Chapter extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	private String title;
	private int number;
	private String style;
	private String content;
	private Date publishDate;
	private boolean isFinal;
	
	private Book book;

	@Column(name="title", nullable=false, length=200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="number", nullable=false)
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Column(name="style", nullable=false, length=20)
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Column(name="content", nullable=true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="publish_date", nullable=true)
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Column(name="is_final", nullable=false)
	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="book_id", nullable=false)
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
