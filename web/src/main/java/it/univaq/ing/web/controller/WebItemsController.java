package it.univaq.ing.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.web.model.Category;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.Product;
import it.univaq.ing.web.services.WebCategoriesService;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebProductsService;

/**
 * 
 * @author LC
 *
 */
@Controller
@SessionAttributes(value={"accountName", "categories"})
public class WebItemsController {

	@Autowired
	protected WebItemsService itemsService;
	
	@Autowired
	protected WebProductsService productsService;
	
	@Autowired
	protected WebCategoriesService categoriesService;
	
	protected Logger logger = Logger.getLogger(WebItemsService.class.getName());

	public WebItemsController(WebItemsService itemsService) {
		this.itemsService = itemsService;
	}
	
	@RequestMapping(value = "/findItemsByIdProduct/{idProduct}")
	public String getItemsByIdProduct(@PathVariable(value="idProduct") Long idProduct, Model model) {
		
		logger.info("START WebItemsController --> getItemsByIdProduct");
		try{
			List<Item> items = itemsService.findItemsByIdProduct(idProduct);
			model.addAttribute("items", items);	
			model.addAttribute("idProduct", idProduct);		
			model.addAttribute("idCategory", items.get(0).getCategoryId());
			model.addAttribute("path", "/shop");
			
		}catch (RestClientException e) {
			logger.info("ERROR WebItemsController --> getItemsByIdProduct: "+e.getMessage());
			return "404";
		}
		logger.info("END WebItemsController --> getItemsByIdProduct");
		return "shop";
	}
	
	@RequestMapping(value = "/findItemsByCategotyId/{idCategory}", method=RequestMethod.GET )
	public String findItemsByCategotyId(@PathVariable(value="idCategory") Long idCategory, Model model) {
		
		logger.info("START WebItemsController --> findItemsByCategotyId");
		try{
			@SuppressWarnings("unchecked")
			List<Category> categories =  (List<Category>) model.asMap().get("categories");
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
			List<Item> items = itemsService.findItemsByIdCategory(idCategory);
			model.addAttribute("categories", categories);
			model.addAttribute("items", items);
			model.addAttribute("idCategory", idCategory);
			model.addAttribute("path", "/shop");
		}catch (RestClientException e) {
			logger.info("ERROR WebItemsController --> findItemsByCategotyId: "+e.getMessage());
			return "404";
		}
		logger.info("END WebItemsController --> findItemsByCategotyId");
		return "shop";
	}
	
	@RequestMapping(value = "/findItemByProductFilter/{idProduct}/{priceMin}/{priceMax}")
	public String findItemByProductFilter(Model model,@PathVariable(value="idProduct") Long idProduct, @PathVariable(value="priceMin") Long priceMin,  @PathVariable(value="priceMax") Long priceMax) {
		
		logger.info("START WebItemsController --> findItemByProductFilter");
		try{
			List<Item> items = itemsService.findItemByProductFilter(idProduct, priceMin, priceMax);
			if(items.size() == 0){
				Product product = productsService.findProductById(idProduct);
				model.addAttribute("idCategory", product.getCategoryId());
			}else{
				model.addAttribute("idCategory", items.get(0).getCategoryId());
			}
			model.addAttribute("items", items);	
			model.addAttribute("idProduct", idProduct);		
			
			model.addAttribute("path", "/shop");
			model.addAttribute("priceMin", priceMin);
			model.addAttribute("priceMax", priceMax);
			
		}catch (RestClientException e) {
			logger.info("ERROR WebItemsController --> findItemByProductFilter: "+e.getMessage());
			return "404";
		}
		logger.info("END WebItemsController --> findItemByProductFilter");
		return "shop";
	}
	
	@RequestMapping(value = "/findItemByCategoryFilter/{idCategory}/{priceMin}/{priceMax}", method=RequestMethod.GET )
	public String findItemByCategoryFilter(Model model, @PathVariable(value="idCategory") Long idCategory, @PathVariable(value="priceMin") Long priceMin,  @PathVariable(value="priceMax") Long priceMax ) {
		
		logger.info("START WebItemsController --> findItemByCategoryFilter");
		try{
			@SuppressWarnings("unchecked")
			List<Category> categories =  (List<Category>) model.asMap().get("categories");
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
			List<Item> items = itemsService.findItemByCategoryFilter(idCategory, priceMin, priceMax);
			model.addAttribute("categories", categories);
			model.addAttribute("items", items);
			model.addAttribute("idCategory", idCategory);
			model.addAttribute("path", "/shop");
			model.addAttribute("priceMin", priceMin);
			model.addAttribute("priceMax", priceMax);
			
		}catch (RestClientException e) {
			logger.info("ERROR WebItemsController --> findItemByCategoryFilter: "+e.getMessage());
			return "404";
		}
		logger.info("END WebItemsController --> findItemByCategoryFilter");
		return "shop";
	}
}