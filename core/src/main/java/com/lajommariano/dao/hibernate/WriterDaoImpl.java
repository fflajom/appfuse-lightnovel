package com.lajommariano.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.lajommariano.dao.WriterDao;
import com.lajommariano.model.Writer;

@Repository("writerDao")
public class WriterDaoImpl extends GenericDaoHibernate<Writer, Long>implements WriterDao{

	public WriterDaoImpl() {
		super(Writer.class);
	}

	@Override
	public Writer loadWriterByUsername(String username)
			throws UsernameNotFoundException {
		List users = getSession().createCriteria(Writer.class).add(Restrictions.eq("username", username)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (Writer) users.get(0);
        }
	}

	@Override
	public List<Writer> getWriters() {
		return this.getAll();
	}

	@Override
	public Writer saveWriter(Writer writer) {
		return this.save(writer);
	}
	
	

}
