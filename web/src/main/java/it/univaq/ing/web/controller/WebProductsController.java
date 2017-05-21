package it.univaq.ing.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.web.model.Category;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.Product;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebProductsService;

/**
 * 
 * @author LC
 */
@Controller
@SessionAttributes(value={"accountName", "categories","countCartItem"})
public class WebProductsController {

	@Autowired
	protected WebProductsService productsService;
	
	@Autowired
	protected WebItemsService itemsService;

	protected Logger logger = Logger.getLogger(WebProductsController.class.getName());

	public WebProductsController(WebProductsService productsService) {
		this.productsService = productsService;
	}

	@RequestMapping(value = "/shop")
	public String getShop(Model model) {
		
		logger.info("START WebProductsController --> getShop");
		@SuppressWarnings("unchecked")
		List<Category> categories =  (List<Category>) model.asMap().get("categories");
		try{
			
			List<Product> products = productsService.findAll();
			for(Category category: categories){
				List<Product> listProduct = new ArrayList<Product>();
				for(Product product : products){
					if(category.getCategoryId().equals(product.getCategoryId())){
						listProduct.add(product);
					}
				}
				category.setListProduct(listProduct);
			}
			List<Item> items = itemsService.findItemsByIdProduct(categories.get(0).getListProduct().get(0).getProductId());
			model.addAttribute("categories", categories);	
			model.addAttribute("idCategory", categories.get(0).getCategoryId());
			model.addAttribute("idProduct", categories.get(0).getListProduct().get(0).getProductId());
			model.addAttribute("products", products);
			model.addAttribute("items", items);	
			model.addAttribute("path", "/shop");	

		}catch(RestClientException e){
			logger.info("ERROR WebProductsController --> getShop: "+ e.getMessage());
			return "404";
		}
		logger.info("END WebProductsController --> getShop");
		return "shop";
	}
	
	@RequestMapping(value = "/product/{category_id}")
	public String getCategory(@PathVariable(value="category_id") Long category_id, Model model) {
		logger.info("START WebProductsController --> getCategory");
		try{
			List<Product> products = productsService.findProductByCategory(category_id);
			model.addAttribute("products", products);			
		}catch(RestClientException e){
			logger.info("ERROR WebProductsController --> getCategory: "+ e.getMessage());
			return "404";
		}
		logger.info("END WebProductsController --> getCategory");
		return "shop";
	}
}