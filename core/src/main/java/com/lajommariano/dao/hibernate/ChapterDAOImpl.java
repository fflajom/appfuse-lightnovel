package com.lajommariano.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.lajommariano.dao.ChapterDAO;
import com.lajommariano.model.Chapter;

@Repository("ChapterDAO")
public class ChapterDAOImpl extends GenericDaoHibernate<Chapter, Long> implements ChapterDAO{

	public ChapterDAOImpl() {
		super(Chapter.class);
	}

}
