package com.lajommariano.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lajommariano.model.Writer;

public class WriterServiceTest extends BaseManagerTestCase{

	@Autowired
	WriterManager manager;
	
	@Test
    public void testGetWriter() throws Exception {
		
        Writer user = manager.get(-2L);

        assertNotNull(user);
        assertEquals(1, user.getRoles().size());
        assertTrue(user.isEnabled());
    }
}
