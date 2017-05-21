package it.univaq.ing.web.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.CartItem;

/**
 * 
 * @author LC
 */
@Service
public class WebCartService {

	@Autowired
	protected RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebCartService.class.getName());

	public WebCartService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}
	
	public CartItem saveCartItem(CartItem cartItem){
		logger.info("START WebCartService --> saveCartItem");
		CartItem cartItemResponse = new CartItem();
		try{
			cartItemResponse = restTemplate.postForObject(serviceUrl + "/saveCartItem", cartItem, CartItem.class);			
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> saveCartItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> saveCartItem");
		return cartItemResponse;
	}
	
	public CartItem updateCartItem(CartItem cartItem){
		logger.info("START WebCartService --> updateCartItem");
		try{
		    restTemplate.put(serviceUrl + "/updateCartItem", cartItem);		
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> updateCartItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> updateCartItem");
		return cartItem;
	}
	
	public void updateListCartItem(String username){
		logger.info("START WebCartService --> updateListCartItem");
		try {
			restTemplate.postForObject(serviceUrl + "/updateListCartItem", username, String.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> updateListCartItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> updateListCartItem");
	}

	public List<CartItem> findListCartItemByUsername(String username) {
		
		logger.info("START WebCartService --> findListCartItemByUsername --> username: "+username);
		CartItem[] cartItems = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);	
			cartItems = restTemplate.getForObject(serviceUrl + "/findCartItem/{username}", CartItem[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> findListCartItemByUsername: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> findListCartItemByUsername --> username: "+username);
		return Arrays.asList(cartItems);
	}
	
	public CartItem findCartByUsernameByIdItem(String username, Long idItem) {

		logger.info("START WebCartService --> findCartByUsernameByIdItem --> username: "+username);
		CartItem cartItem = new CartItem();
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);
			vars.put("idItem", idItem.toString());
			cartItem = restTemplate.getForObject(serviceUrl + "/findCartByUsernameByIdItem/{username}/{idItem}", CartItem.class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> findCartByUsernameByIdItem: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> findCartByUsernameByIdItem --> username: "+username);
		return cartItem;
	}
	
	public Integer countCartItemByUsername(String username) {
		
		logger.info("START WebCartService --> countCartItemByUsername --> username: "+username);
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);	
			return restTemplate.getForObject(serviceUrl + "/countCartItem/{username}", Integer.class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> countCartItemByUsername --> username: "+e.getMessage());
			throw e;
		}
	}
	
	public void deleteCartItemById(Long idCartItem){
		
		logger.info("START WebCartService --> deleteCartItemById");
		Map<String, String> vars = new HashMap<String, String>();
		try{
			vars.put("idCartItem", idCartItem.toString());	
			restTemplate.delete(serviceUrl + "/deleteCartItemByIdCartItem/{idCartItem}", vars);			
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> deleteCartItemById: "+e.getMessage());
			throw e;
		}	
	}
		
	public void deleteCartItemByUsername(String idAccount){
		
		logger.info("START WebCartService --> deleteCartItemByUsername");
		Map<String, String> vars = new HashMap<String, String>();
		try{
			vars.put("idAccount", idAccount);	
			restTemplate.delete(serviceUrl + "/deleteCartItem/{idAccount}", vars);			
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> deleteCartItemByUsername: "+e.getMessage());
			throw e;
		}		
	}
	
	public CartItem findCartItemById(Long idCart) {
		
		logger.info("START WebCartService --> findCartItemById");
		CartItem cartItem = new CartItem();
		Map<String, Long> vars = new HashMap<String, Long>();
		try {
			vars.put("idCart", idCart);	
			cartItem = restTemplate.getForObject(serviceUrl + "/findCartItemById/{idCart}", CartItem.class, vars);	
		}catch (RestClientException e) {		
			logger.info("ERROR WebCartService --> deleteCartItemByUsername: "+e.getMessage());
			throw e;
		}
		logger.info("END WebCartService --> findCartItemById");
		return cartItem;
	}	
}