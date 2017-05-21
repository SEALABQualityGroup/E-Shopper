package it.univaq.ing.web.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.univaq.ing.security.model.Account;
import it.univaq.ing.web.services.WebLoginService;

/**
 * 
 * @author LC
 *
 */
@Controller
@SessionAttributes(value={"accountName", "countCartItem", "products", "itemsRecommended", "items","featuresItems", "categories"})
public class WebLoginController {

	@Autowired
	protected WebLoginService loginService;
	
	protected Logger logger = Logger.getLogger(WebLoginService.class.getName());
	
	public WebLoginController(WebLoginService loginService) {
		this.loginService = loginService;
	}

	@RequestMapping(value = "/returnLogin", method = RequestMethod.GET )
	public String returnLogin(Model model) {
		
		logger.info("START WebLoginController --> returnLogin");
		Account account = new Account();
		model.addAttribute("account", account);
		model.addAttribute("noAccount", Boolean.FALSE);
		model.addAttribute("returnLogin", Boolean.TRUE);
		model.addAttribute("path", "/login");
		logger.info("END WebLoginController --> returnLogin");
		return "login";
	}	
}