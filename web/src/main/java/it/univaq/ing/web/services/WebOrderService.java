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

import it.univaq.ing.web.model.HistoryOrder;
import it.univaq.ing.web.model.Order;
import it.univaq.ing.web.model.RelCartOrder;
import it.univaq.ing.web.model.StatusOrder;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author LC
 */
@Service
public class WebOrderService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebOrderService.class.getName());

	public WebOrderService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}	

	public Order saveOrder(Order order){
		logger.info("START WebOrderService --> saveOrder");
		Order orderResponse = new Order();
		try{	
			orderResponse = restTemplate.postForObject(serviceUrl + "/saveOrder", order, Order.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> saveOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> saveOrder");
		return orderResponse;
	}
	
	public HistoryOrder saveHistoryOrder(HistoryOrder historyOrder){
		logger.info("START WebOrderService --> saveHistoryOrder");
		HistoryOrder historyOrderResponse = new HistoryOrder();
		try{	
			historyOrderResponse = restTemplate.postForObject(serviceUrl + "/saveHistoryOrder",historyOrder, HistoryOrder.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> saveHistoryOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> saveHistoryOrder");
		return historyOrderResponse;
	}
	
	public RelCartOrder saveRelCartOrder(RelCartOrder relCartOrder){
		logger.info("START WebOrderService --> saveRelCartOrder");
		RelCartOrder relCartOrderResponse = new RelCartOrder();
		try{	
			relCartOrderResponse = restTemplate.postForObject(serviceUrl + "/saveRelCartOrder",relCartOrder, RelCartOrder.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> saveRelCartOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> saveRelCartOrder");
		return relCartOrderResponse;
	}
	
	public StatusOrder findStatusOrderById(Long idStatusOrder) {
		
		logger.info("START WebOrderService --> findStatusOrderById");
		StatusOrder statusOrder = new StatusOrder();
		Map<String, String> vars = new HashMap<String, String>();		
		try {
			vars.put("idStatusOrder", idStatusOrder.toString());
			statusOrder = restTemplate.getForObject(serviceUrl + "/findStatusOrderByIdStatusOrder/{idStatusOrder}", StatusOrder.class, vars);	
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> findStatusOrderById: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> findStatusOrderById");
		return statusOrder;
	}

	public List<Order> findListOrder(String username, String statusOrder){
		
		logger.info("START WebOrderService --> findListOrder --> username: "+username);
		Order[] listOrder = null;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);
			vars.put("statusOrder", statusOrder);	
			listOrder =  restTemplate.getForObject(serviceUrl + "/findListOrder/{username}/{statusOrder}", Order[].class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> findListOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> findListOrder --> username: "+username);
		return Arrays.asList(listOrder);
	}
	
	
	public Order findOrder(Long idOrder){
		
		logger.info("START WebOrderService --> findOrder");
		Order order= null;
		Map<String, Long> vars = new HashMap<String, Long>();
		try {
			vars.put("idOrder", idOrder);
			order =  restTemplate.getForObject(serviceUrl + "/order/{idOrder}", Order.class, vars);
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> findOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> findOrder");
		return order;
	}
	
	public void updateOrder(Order order){
		
		logger.info("START WebOrderService --> updateOrder");
		try{
		    restTemplate.put(serviceUrl + "/updateOrder", order);		
		}catch (RestClientException e) {		
			logger.info("ERROR WebOrderService --> updateOrder: "+e.getMessage());
			throw e;
		}
		logger.info("END WebOrderService --> updateOrder");

	}
}