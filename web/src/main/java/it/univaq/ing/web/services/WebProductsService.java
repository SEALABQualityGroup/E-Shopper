package it.univaq.ing.web.services;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.Product;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author LC
 */
@Service
public class WebProductsService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(WebProductsService.class
			.getName());

	public WebProductsService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}


	public List<Product> findAll(){
		
		logger.info("START WebProductsService --> findAll");
		Product[] products = null;
		try {
			products = restTemplate.getForObject(serviceUrl	+ "/findProduct", Product[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebProductsService --> findAll: "+e.getMessage());
			throw e;
		}
		logger.info("START WebProductsService --> findAll");
		return Arrays.asList(products);
	}

	public List<Product> findProductByCategory(Long category_id) {
		
		logger.info("START WebProductsService --> findProductByCategory");
		Product[] products = null;
		try {
			products = restTemplate.getForObject(serviceUrl + "/product/"+ category_id, Product[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebProductsService --> findProductByCategory: "+e.getMessage());
			throw e;
		}
		logger.info("END WebProductsService --> findProductByCategory");
		return Arrays.asList(products);
	}
	
	public Product findProductById(Long productid) {
		
		logger.info("START WebProductsService --> findProductById");
		Product product = null;
		try {
			product = restTemplate.getForObject(serviceUrl + "/findProductById/"+ productid, Product.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebProductsService --> findProductById: "+e.getMessage());
			throw e;
		}
		logger.info("END WebProductsService --> findProductByCategory");
		return product;
	}
	
	public List<Product> findProductsRandom(){
		
		logger.info("START WebProductsService --> findProductsRandom");
		Product[] products = null;
		try {
			products = restTemplate.getForObject(serviceUrl	+ "/findProductsRandom", Product[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebProductsService --> findProductsRandom: "+e.getMessage());
			throw e;
		}
		logger.info("END WebProductsService --> findProductsRandom");
		return Arrays.asList(products);
	}
}