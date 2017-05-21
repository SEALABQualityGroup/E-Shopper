package it.univaq.ing.web.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.WishList;

/**
 * 
 * @author LC
 */
@Service
public class WebWishListService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebWishListService.class.getName());

	public WebWishListService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}
	
	public WishList saveWishList(WishList wishList){
		
		logger.info("START WebWishListService --> saveWishList");
		WishList wishListResponse = new WishList();
		try{
			 wishListResponse = restTemplate.postForObject(serviceUrl + "/saveWishList", wishList, WishList.class);			
		}catch (RestClientException e) {		
			logger.info("ERROR WebWishListService --> saveWishList: "+e.getMessage());
			throw e;
		}
		logger.info("START WebWishListService --> saveWishList");
		return wishListResponse;
	}

	public List<WishList> findWishListByUsername(String username) {

		logger.info("START WebWishListService --> findWishListByUsername --> username: "+username);
		WishList[] wishLists = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);	
			wishLists = restTemplate.getForObject(serviceUrl + "/findWishList/{username}", WishList[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebWishListService --> findWishListByUsername: "+e.getMessage());
			throw e;
		}
		logger.info("END WebWishListService --> findWishListByUsername --> username: "+username);
		return Arrays.asList(wishLists);
	}
	
	public WishList findWishListByUsernameByIdItem(String username, Long idItem) {
		
		logger.info("START WebWishListService --> findWishListByUsernameByIdItem --> username: "+username);
		WishList wishList = new WishList();
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);
			vars.put("idItem", idItem.toString());	
			wishList = restTemplate.getForObject(serviceUrl + "/findWishListByUsernameByIdItem/{username}/{idItem}", WishList.class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebWishListService --> findWishListByUsernameByIdItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebWishListService --> findWishListByUsernameByIdItem --> username: "+username);
		return wishList;
	}

	public void deleteWishListById(Long idWishList){
		
		logger.info("START WebWishListService --> deleteWishListById");
		Map<String, String> vars = new HashMap<String, String>();
		try{
			vars.put("idCartItem", idWishList.toString());	
			restTemplate.delete(serviceUrl + "/deleteWishListById/{idCartItem}", vars);			
		}catch (RestClientException e) {		
			logger.info("ERROR WebWishListService --> deleteWishListById: "+e.getMessage());
			throw e;
		}
		logger.info("END WebWishListService --> deleteWishListById");
	}
}
