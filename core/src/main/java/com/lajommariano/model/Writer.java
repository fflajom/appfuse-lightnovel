package com.lajommariano.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("W")
public class Writer extends User {

	private static final long serialVersionUID = 1L;

	private List<Book> books = new ArrayList<Book>();

	@OneToMany(fetch=FetchType.EAGER, mappedBy="writer")
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "Writer [books=" + books + ", username=" + username + "]";
	}
}
