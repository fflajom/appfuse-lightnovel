package com.lajommariano.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.lajommariano.dao.TagDAO;
import com.lajommariano.model.Tag;

@Repository("TagDAO")
public class TagDAOImpl extends GenericDaoHibernate<Tag, Long> implements TagDAO {

	public TagDAOImpl() {
		super(Tag.class);
	}

}
