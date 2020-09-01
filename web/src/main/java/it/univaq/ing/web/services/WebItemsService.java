package it.univaq.ing.web.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.Item;

/**
 * 
 * @author LC
 *
 */
@Service
public class WebItemsService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebItemsService.class.getName());

	public WebItemsService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}
	
	public List<Item> findItemsByIdProduct(Long idProduct) {
		
		logger.info("START WebItemsService --> findItemsByIdProduct");
		Item[] items = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("idProduct", idProduct.toString());
			items = restTemplate.getForObject(serviceUrl + "/findItems/{idProduct}", Item[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemsByIdProduct: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemsByIdProduct");
		return Arrays.asList(items);
	}

	public List<Item> findItemsByIdCategory(Long idCategory) {
		
		logger.info("START WebItemsService --> findItemsByIdCategory");
		Item[] items = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("idCategory", idCategory.toString());	
			items = restTemplate.getForObject(serviceUrl + "/findItemsByIdCategory/{idCategory}", Item[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemsByIdCategory: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemsByIdCategory");
		return Arrays.asList(items);
	}
	
	public Item findItemById(Long idItem) {
		
		logger.info("START WebItemsService --> findItemById");
		Item item = new Item();
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("idItem", idItem.toString());	
			item = restTemplate.getForObject(serviceUrl + "/findItem/{idItem}", Item.class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemsByIdCategory: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemById");
		return item;
	}
	
	public List<Item> findItemByProductFilter(Long idProduct, Long priceMin, Long priceMax) {
		
		logger.info("START WebItemsService --> findItemByProductFilter");
		Item[] items = null;
		Map<String, Long> vars = new HashMap<String, Long>();
		try {
			vars.put("idProduct", idProduct);	
			vars.put("priceMin", priceMin);	
			vars.put("priceMax", priceMax);	
			items = restTemplate.getForObject(serviceUrl + "/findItemByProductFilter/{idProduct}/{priceMin}/{priceMax}", Item[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemByProductFilter: "+e.getMessage());
			if(e.getMessage().equals("500")){
				return new ArrayList<Item>();
			}
			throw e;
		}
		logger.info("END WebItemsService --> findItemByProductFilter");
		return Arrays.asList(items);
	}
	
	
	public List<Item> findItemByCategoryFilter(Long idCategory, Long priceMin, Long priceMax) {
		
		logger.info("START WebItemsService --> findItemByCategoryFilter");
		Item[] items = null;
		Map<String, Long> vars = new HashMap<String, Long>();
		try {
			vars.put("idCategory", idCategory);	
			vars.put("priceMin", priceMin);	
			vars.put("priceMax", priceMax);	
			items = restTemplate.getForObject(serviceUrl + "/findItemByCategoryFilter/{idCategory}/{priceMin}/{priceMax}", Item[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemByCategoryFilter: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemByCategoryFilter");
		return Arrays.asList(items);
	}
	
	
	public List<Item> findItemsRandomByIdProduct(Long idProduct) {
		
		logger.info("START WebItemsService --> findItemsRandomByIdProduct");
		Item[] items = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("idProduct", idProduct.toString());	
			items = restTemplate.getForObject(serviceUrl + "/findItemsRandomByProductId/{idProduct}", Item[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemsRandomByIdProduct: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemsRandomByIdProduct");
		return Arrays.asList(items);
	}
	
	@Async
	public CompletableFuture<List<Item>> findItemsRandom() {
		
		logger.info("START WebItemsService --> findItemsRandom");
		Item[] items = null;
		try {
			items = restTemplate.getForObject(serviceUrl + "/findItemsRandom", Item[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findItemsRandom: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> findItemsRandom");
		List<Item> results = Arrays.asList(items);
		return CompletableFuture.completedFuture(results);
	}
	
	public List<Item> findFeaturesItemRandom() {
		
		logger.info("START WebItemsService --> findFeaturesItemRandom");
		Item[] items = null;
		try {
			items = restTemplate.getForObject(serviceUrl + "/findFeaturesItemRandom", Item[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> findFeaturesItemRandom: "+e.getMessage());
			throw e;
		}
		List<Item> results = Arrays.asList(items);
		return results;
	}
	
	public Item updateItem(Item item){
		
		logger.info("START WebItemsService --> updateItem");
		Item respItem = null;
		try {
			respItem =  restTemplate.postForObject(serviceUrl + "/updateItem", item, Item.class);
		} catch (RestClientException e) {		
			logger.info("ERROR WebItemsService --> updateItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebItemsService --> updateItem");
		return respItem;
	}
}