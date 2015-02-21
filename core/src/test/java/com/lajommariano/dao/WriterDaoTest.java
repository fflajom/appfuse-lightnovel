package com.lajommariano.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lajommariano.model.User;
import com.lajommariano.model.Writer;

public class WriterDaoTest extends BaseDaoTestCase {
    
	@Autowired
	WriterDao dao;
	
	@Test
	public void testGetWriter() throws Exception {
        Writer user = dao.get(-2L);

        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.isEnabled());
    }


}
