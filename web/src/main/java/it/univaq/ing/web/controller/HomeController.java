package it.univaq.ing.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.security.UserPrincipal;
import it.univaq.ing.security.model.Account;
import it.univaq.ing.web.model.Category;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.Product;
import it.univaq.ing.web.services.WebCartService;
import it.univaq.ing.web.services.WebCategoriesService;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebProductsService;
import it.univaq.ing.web.util.Experiment;
import it.univaq.ing.web.util.SyntheticNoise;

/**
 * Home page controller.
 * 
 * @author LC
 */
@Controller
@SessionAttributes(value = { "accountName", "products", "itemsRecommended", "featuresItems", "categories",
		"countCartItem" })
public class HomeController {

	@Value("#{'${experiment.home}'.split(',')}")
	List<String> latencyInjections;

	@Value("#{'${noise.home}'.split(',')}")
	List<String> noiseInjections;

	@Autowired
	Tracer tracer;

	@Autowired
	protected WebCategoriesService categoriesService;

	@Autowired
	protected WebProductsService productsService;

	@Autowired
	protected WebItemsService itemsService;

	@Autowired
	protected WebCartService cartService;

	protected Logger logger = Logger.getLogger(HomeController.class.getName());

	protected static Random random = new Random(33);

	private void addRequestClass() {
		int requestClass = random.nextInt(latencyInjections.size());
		Span span = tracer.getCurrentSpan();
		span.setBaggageItem("experiment", Integer.toString(requestClass));
		span.tag("experiment", Integer.toString(requestClass));
	}

	@RequestMapping("/")
	public String home(Model model) throws InterruptedException, ExecutionException {
		this.addRequestClass();

		logger.info("START HomeController --> home");

		try {
			this.getHomeProduct(model);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.isAuthenticated()
					&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				Integer countCartItem = cartService.countCartItemByUsername(accountSession.getUsername());
				model.addAttribute("countCartItem", countCartItem);
				model.addAttribute("account", accountSession);
				model.addAttribute("accountName", accountSession.getName());
			} else {
				Account account = new Account();
				model.addAttribute("account", account);
				model.addAttribute("noAccount", Boolean.FALSE);
			}
		} catch (RestClientException e) {
			logger.info("ERROR HomeController --> home: " + e.getMessage());
			return "404";
		}
		logger.info("END HomeController --> home");
		return "index";
	}

	@RequestMapping("/findItemsRandomByProductId/{idProduct}")
	public String findItemsRandomByProductId(@PathVariable(value = "idProduct") Long idProduct, Model model) {

		logger.info("START HomeController --> findItemsRandomByProductId");
		try {
			model.addAttribute("path", "/");
			List<Item> items = itemsService.findItemsRandomByIdProduct(idProduct);
			model.addAttribute("items", items);
			model.addAttribute("idProduct", idProduct);
		} catch (RestClientException e) {
			logger.info("ERROR HomeController --> findItemsRandomByProductId: " + e.getMessage());
			return "404";
		}
		logger.info("END HomeController --> findItemsRandomByProductId");
		return "index";
	}

	private void getHomeProduct(Model model) throws InterruptedException, ExecutionException {
		model.addAttribute("path", "/");
		CompletableFuture<List<Item>> futureRecommendedItems = itemsService.findItemsRandom();
		List<Item> featuresItems = itemsService.findFeaturesItemRandom();

		Span span = tracer.getCurrentSpan();
		Experiment.injectLatency(span, latencyInjections);
		SyntheticNoise.injectLatency(span, noiseInjections);

		List<Product> productLsit = productsService.findAll();
		List<Category> categories = categoriesService.findAll();
		for(Category category: categories){
			List<Product> listProduct = new ArrayList<Product>();
			for(Product product : productLsit){
				if(category.getCategoryId().equals(product.getCategoryId())){
					listProduct.add(product);
				}
			}
			category.setListProduct(listProduct);
		}
		model.addAttribute("categories", categories);
		List<Product> products = productsService.findProductsRandom();
		for(Product product : products){
			List<Item> items = itemsService.findItemsRandomByIdProduct(product.getProductId());
			product.setListItem(items);
		}		
		String accountName =  (String) model.asMap().get("accountName");

		if(accountName == null){
			model.addAttribute("accountName", "");
		}
		model.addAttribute("idProduct", products.get(0).getProductId());
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		model.addAttribute("items", products.get(0).getListItem());
		model.addAttribute("featuresItems", featuresItems);
		model.addAttribute("itemsRecommended", futureRecommendedItems.get());

	}
}
