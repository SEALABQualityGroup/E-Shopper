package it.univaq.ing.web.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.web.model.Category;
import it.univaq.ing.web.services.WebCategoriesService;
import it.univaq.ing.web.services.WebLoginService;
import it.univaq.ing.web.services.WebProductsService;

/**
 * 
 * @author LC
 *
 */
@Controller
@SessionAttributes(value = { "accountName" })
public class WebCategoriesController {

	@Autowired
	protected WebCategoriesService categoriesService;

	@Autowired
	protected WebProductsService productsService;

	protected Logger logger = Logger.getLogger(WebLoginService.class.getName());

	public WebCategoriesController(WebCategoriesService categoriesService) {
		this.categoriesService = categoriesService;
	}

	@RequestMapping(value = "/category")
	public String getCategory(Model model) throws InterruptedException, ExecutionException {
		
		logger.info("START WebCategoriesController --> getCategory");
		try{
			List<Category> categories = categoriesService.findAll();
			model.addAttribute("categories", categories);
			logger.info("END WebCategoriesController --> getCategory");
			return "category";
		}catch(RestClientException e){
			logger.info("ERROR WebCategoriesController --> getCategory: "+ e.getMessage());
			return "404";
		}
	}
}