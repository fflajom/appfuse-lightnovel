package com.lajommariano.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lajommariano.model.Writer;
import com.lajommariano.service.UserManager;
import com.lajommariano.service.WriterManager;
import com.lajommariano.service.model.UserDTO;

@Controller
@RequestMapping("/home*")
public class HomeController {

	private transient final Log log = LogFactory.getLog(HomeController.class);
    
	@Autowired
	private UserManager manager;
	
	@Autowired
	WriterManager writerManager;

	
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest(
    		HttpServletRequest request,
            HttpServletResponse response) throws Exception {
                
    	Model model = new ExtendedModelMap();
    	UserDTO user = (UserDTO) manager.loadUserByUsername(
    										request.getUserPrincipal().getName());
    	
    	//change to constant
    	if(user.getRoleList().get(0).getLabel().equals("ROLE_ADMIN")){
    		Writer writer = writerManager.get(user.getId());
            model.addAttribute("books", writer.getBooks());
            		
            return new ModelAndView("writer/writerHome", model.asMap());
    	}else{
    		return new ModelAndView("home",model.asMap());
    	}
    }
}
