package com.lajommariano.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lajommariano.dao.WriterDao;
import com.lajommariano.model.Writer;
import com.lajommariano.service.WriterManager;
import com.lajommariano.service.model.WriterDTO;
import com.lajommariano.util.DozerHelper;

@Service("writerManager")
public class WriterManagerImpl extends GenericManagerImpl<Writer, Long> implements WriterManager  {

	
	private WriterDao writerDao;
	
	@Autowired
	private DozerHelper mapper;
	
	@Autowired
	public WriterManagerImpl(WriterDao writerDao){
		super(writerDao);
		this.writerDao = writerDao;
	}
	
	@Override
	public WriterDTO loadWriterByUsername(String username)
			throws UsernameNotFoundException {
		return mapper.map(writerDao.loadWriterByUsername(username),WriterDTO.class);
	}

	@Override
	public List<WriterDTO> getWriters() {
		return mapper.map(writerDao.getWriters(),WriterDTO.class);
	}

	@Override
	public WriterDTO saveWriter(WriterDTO writer) {
		return mapper.map(writerDao.saveWriter(mapper.map(writer,Writer.class)),WriterDTO.class);
	}
	
	public WriterDTO getById(Long id){
		return mapper.map(get(id), WriterDTO.class);
	}

}
