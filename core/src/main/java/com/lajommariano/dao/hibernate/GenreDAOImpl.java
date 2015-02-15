package com.lajommariano.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.lajommariano.dao.GenreDAO;
import com.lajommariano.model.Genre;

@Repository("GenreDAO")
public class GenreDAOImpl extends GenericDaoHibernate<Genre, Long> implements GenreDAO {

	public GenreDAOImpl() {
		super(Genre.class);
	}

}
