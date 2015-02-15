package com.lajommariano.service;

import com.lajommariano.service.model.UserDTO;
import com.lajommariano.model.User;
import com.lajommariano.util.DozerHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MockUserDetailsService implements UserDetailsService {
	
	@Autowired
	private DozerHelper helper;
	
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return helper.map(new User("testuser"), UserDTO.class);
    }
}
