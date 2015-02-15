package com.lajommariano.service.impl;

import com.lajommariano.service.model.UserDTO;
import com.lajommariano.Constants;
import com.lajommariano.dao.UserDao;
import com.lajommariano.model.Role;
import com.lajommariano.model.User;
import com.lajommariano.service.UserManager;
import com.lajommariano.service.UserSecurityAdvice;
import com.lajommariano.util.DozerHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserSecurityAdviceTest {

	@Autowired
	private DozerHelper helper;
	
    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    ApplicationContext ctx = null;
    SecurityContext initialSecurityContext = null;

    @Before
    public void setUp() throws Exception {
        // store initial security context for later restoration
        initialSecurityContext = SecurityContextHolder.getContext();

        SecurityContext context = new SecurityContextImpl();
        UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setId(1L);
        user.setPassword("password");
        user.addRole(new Role(Constants.USER_ROLE));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(initialSecurityContext);
    }

    @Test
    public void testAddUserWithoutAdminRole() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth.isAuthenticated());
        UserManager userManager = makeInterceptedTarget();
        helper = getHelper();
        User user = new User("admin");
        user.setId(2L);

        try {
            userManager.saveUser(helper.map(user,UserDTO.class));
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            Assert.assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    @Test
    public void testAddUserAsAdmin() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        helper = getHelper();
        
        UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setId(2L);
        user.setPassword("password");
        user.addRole(new Role(Constants.ADMIN_ROLE));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        UserManager userManager = makeInterceptedTarget();
        
        final User adminUser = new User("admin1");
        adminUser.setId(2L);

        given(userDao.saveUser(adminUser)).willReturn(adminUser);
        given(passwordEncoder.encode(adminUser.getPassword())).willReturn(adminUser.getPassword());
        
        System.out.println(adminUser);
        System.out.println(helper.map(adminUser,UserDTO.class));
        
        userManager.saveUser(helper.map(adminUser,UserDTO.class));
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        helper = getHelper();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.USER_ROLE));
        
        given(userDao.saveUser(user)).willReturn(user);
        given(passwordEncoder.encode(user.getPassword())).willReturn(user.getPassword());

        userManager.saveUser(helper.map(user,UserDTO.class));
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        helper = getHelper();
        User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));

        try {
            userManager.saveUser(helper.map(user, UserDTO.class));
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testAddAdminRoleWhenAlreadyHasUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        helper = getHelper();
        User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        try {
            userManager.saveUser(helper.map(user, UserDTO.class));
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testAddUserRoleWhenHasAdminRole() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        UserDTO user1 = new UserDTO();
        user1.setUsername("user1");
        user1.setId(1L);
        user1.setPassword("password");
        user1.addRole(new Role(Constants.ADMIN_ROLE));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword(), user1.getAuthorities());
        token.setDetails(user1);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        helper = getHelper();
        UserManager userManager = makeInterceptedTarget();
        final User user = new User("user1");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        given(userDao.saveUser(user)).willReturn(user);
        given(passwordEncoder.encode(user.getPassword())).willReturn(user.getPassword());

        userManager.saveUser(helper.map(user, UserDTO.class));
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        helper = getHelper();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.USER_ROLE));

        given(userDao.saveUser(user)).willReturn(user);
        given(passwordEncoder.encode(user.getPassword())).willReturn(user.getPassword());

        userManager.saveUser(helper.map(user, UserDTO.class));
    }

    private UserManager makeInterceptedTarget() {
        ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

        UserManager userManager = (UserManager) ctx.getBean("target");
        userManager.setUserDao(userDao);
        userManager.setPasswordEncoder(passwordEncoder);
        userManager.setMapper(getHelper());
        return userManager;
    }
    
    private DozerHelper getHelper(){
    	ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

        return (DozerHelper) ctx.getBean("mapper");
    }
}
