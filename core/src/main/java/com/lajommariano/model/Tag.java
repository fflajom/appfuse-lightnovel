package com.lajommariano.model;

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
	name="tag",
	uniqueConstraints = { @UniqueConstraint(columnNames = { "value", "book_id" }) }
)
public class Tag extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	
	private String value;
	private Book book;

	@Column(name="value", nullable=false, length=25)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
