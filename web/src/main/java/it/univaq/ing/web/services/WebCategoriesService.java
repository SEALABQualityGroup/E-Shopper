package it.univaq.ing.web.services;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.Category;

/**
 * 
 * @author LC
 *
 */
@Service
@FeignClient(name = "account-service")
public class WebCategoriesService {
	
	@Autowired
	protected RestTemplate restTemplate;


	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebCategoriesService.class.getName());

	public WebCategoriesService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://user:password@" + serviceUrl;
	}
	
	public List<Category> findAll(){
		logger.info("START WebCategoriesService --> findAll");	
		Category[] categories = null;
		try {
			categories = restTemplate.getForObject(serviceUrl + "/category", Category[].class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebCategoriesService --> findAll: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCategoriesService --> findAll");		
		List<Category> results = Arrays.asList(categories);
		return results;
	}
}