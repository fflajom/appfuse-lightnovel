package com.lajommariano.service.model;

import java.util.List;

import com.lajommariano.model.Book;

public class WriterDTO extends UserDTO {

	private static final long serialVersionUID = 1L;

	private List<Book> books;

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
