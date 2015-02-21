package com.lajommariano.dao;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.lajommariano.model.Writer;

public interface WriterDao extends GenericDao<Writer,Long> {
    
	@Transactional
    Writer loadWriterByUsername(String username) throws UsernameNotFoundException;

    List<Writer> getWriters();

    Writer saveWriter(Writer writer);

}
