package com.lajommariano.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.lajommariano.dao.BookDAO;
import com.lajommariano.model.Book;

@Repository("BookDAO")
public class BookDAOImpl extends GenericDaoHibernate<Book, Long> implements BookDAO {

	public BookDAOImpl() {
		super(Book.class);
	}

}
