package com.lajommariano.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lajommariano.service.UserManager;
import com.lajommariano.service.model.UserDTO;

@Controller
@RequestMapping("/home*")
public class HomeController {

	private transient final Log log = LogFactory.getLog(HomeController.class);
    
	@Autowired
	private UserManager manager; 
	
    @RequestMapping(method = RequestMethod.GET)
    public String handleRequest(
    		HttpServletRequest request,
            HttpServletResponse response) throws Exception {
                
    	UserDTO user = (UserDTO) manager.loadUserByUsername(request.getUserPrincipal().getName());
    	log.debug(user);
    	if(user.getRoleList().get(0).getLabel().equals("ROLE_ADMIN")){
    		return "admin/activeUsers";
    	}else{
    		return "home";
    	}
    }
}
