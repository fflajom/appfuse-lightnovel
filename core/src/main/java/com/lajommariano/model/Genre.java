package com.lajommariano.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Genre")
public class Genre extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private List<Book> books = new ArrayList<Book>();

	@Column(name="name", nullable=false, unique=true, length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(fetch=FetchType.LAZY, mappedBy="genres")
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
}
