package com.lajommariano.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lajommariano.model.Writer;
import com.lajommariano.service.model.WriterDTO;

public interface WriterManager extends GenericManager<Writer, Long> {

    WriterDTO loadWriterByUsername(String username) throws UsernameNotFoundException;

    List<WriterDTO> getWriters();

    WriterDTO saveWriter(WriterDTO writer);
    
    WriterDTO getById(Long id);
}
