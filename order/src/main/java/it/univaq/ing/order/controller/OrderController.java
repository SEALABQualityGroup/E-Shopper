package it.univaq.ing.order.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.order.OrderException;
import it.univaq.ing.order.domain.HistoryOrder;
import it.univaq.ing.order.domain.Order;
import it.univaq.ing.order.domain.RelCartOrder;
import it.univaq.ing.order.domain.StatusOrder;
import it.univaq.ing.order.repository.HistoryOrderRepository;
import it.univaq.ing.order.repository.OrderRepository;
import it.univaq.ing.order.repository.RelCartOrderRepository;
import it.univaq.ing.order.repository.StatusOrderRepository;

/**
 * 
 * @author LC
 */
@RestController
public class OrderController {
	
	protected Logger logger = Logger.getLogger(OrderController.class.getName());

	protected OrderRepository orderRepository;
	protected HistoryOrderRepository historyOrderRepository;
	protected StatusOrderRepository statusOrderRepository;	
	protected RelCartOrderRepository relCartOrderRepository;
	
	private static final Integer ID_STATUS_ORDER_OPEN =1; 
	private static final Integer ID_STATUS_ORDER_SEND =5;
	private static final Integer ID_STATUS_ORDER_CLOSED =2;
	private static final Integer ID_STATUS_ORDER_CANCELLED =3;
	private static final Integer ID_STATUS_ORDER_ARCHIVED =4;
	
	private static final String ORDER_CANCELLED = "cancelled";
	private static final String ORDER_ARCHIVED = "archived";
	private static final String ORDER_CLOSED = "closed";
	private static final String ORDER_ALL = "all";
	
	protected Random randomGenerator = new Random();

	@Autowired
	public OrderController(OrderRepository orderRepository, HistoryOrderRepository historyOrderRepository, StatusOrderRepository statusOrderRepositor, RelCartOrderRepository relCartOrderRepository) {
		this.orderRepository = orderRepository;
		this.historyOrderRepository = historyOrderRepository;
		this.statusOrderRepository = statusOrderRepositor;
		this.relCartOrderRepository = relCartOrderRepository;

	}

	@RequestMapping(value="/saveOrder", method=RequestMethod.POST)
	public Order saveOrder(@RequestBody Order order){
		logger.info("START OrderController --> saveOrder");
		Order orderResponse = new Order();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
		try{
			if(order.getIdGift().equals(0l)){
				order.setIdGift(null);
			}
			order.setCode("O-"+ RandomStringUtils.randomNumeric(4)+"-"+ RandomStringUtils.randomNumeric(4)+"-"+ RandomStringUtils.randomNumeric(4)+"-" +randomGenerator.nextInt(10));
			orderResponse = orderRepository.save(order);
			
			if(orderResponse != null){
				HistoryOrder historyOrder = new HistoryOrder();
				historyOrder.setOrder(orderResponse);
				historyOrder.setStatusOrder(orderResponse.getIdStatus());
				historyOrder.setStatusDate(sqldate);
				historyOrder.setInsertDate(sqldate);
				historyOrder.setModifyDate(sqldate);
				historyOrderRepository.save(historyOrder); // We are not interested in informing you if history is not successful
			}
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> saveOrder "+ e.getMessage());
			throw new OrderException("save order error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> saveOrder");
		return orderResponse;
	}
	
	@RequestMapping(value="/saveHistoryOrder", method=RequestMethod.POST)
	public HistoryOrder saveHistoryOrder(@RequestBody HistoryOrder historyOrder){
		logger.info("START OrderController --> saveHistoryOrder");
		try{
			return historyOrderRepository.save(historyOrder);
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> saveHistoryOrder "+ e.getMessage());
			throw new OrderException("save history error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
	}
	
	@RequestMapping(value="/saveRelCartOrder", method=RequestMethod.POST)
	public RelCartOrder saveRelCartOrder(@RequestBody RelCartOrder relCartOrder){
		logger.info("START OrderController --> saveRelCartOrder");
		try{
			return relCartOrderRepository.save(relCartOrder);
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> saveRelCartOrder "+ e.getMessage());
			throw new OrderException("save relCartOrder error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
	}
	
	@RequestMapping(value="/updateOrder", method=RequestMethod.PUT)
	public Order updateOrder(@RequestBody Order order) {
		logger.info("START OrderController --> updateOrder");
		Order orderResp = new Order();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
		try{
			orderResp = orderRepository.save(order);
			if(orderResp != null){
				HistoryOrder historyOrder = new HistoryOrder();
				historyOrder.setOrder(orderResp);
				historyOrder.setStatusOrder(orderResp.getIdStatus());
				historyOrder.setStatusDate(sqldate);
				historyOrder.setInsertDate(sqldate);
				historyOrder.setModifyDate(sqldate);
				historyOrderRepository.save(historyOrder); // We are not interested in informing you if history is not successful
			}
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> updateOrder "+ e.getMessage());
			throw new OrderException("update order error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> updateOrder");
		return orderResp;
	}
	
	@RequestMapping(value="/orders/{username}", method=RequestMethod.GET)
	public List<Order> getOrdersByUsername(@PathVariable(value="username") String username) {
		logger.info("START OrderController --> getOrdersByUsername --> username: "+username);
		List<Order> listOrder = new ArrayList<Order>();
		try{
			listOrder = orderRepository.findAllByUser(username);
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> getOrdersByUsername "+ e.getMessage());
			throw new OrderException("find order (where username) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> getOrdersByUsername --> username: "+username);
		return listOrder;
	}
	
	@RequestMapping(value="/order/{idOrder}", method=RequestMethod.GET)
	public Order getOrder(@PathVariable(value="idOrder") Long idOrder) {

		logger.info("START OrderController --> getOrder");
		Order order = new Order();
		try{
			order = orderRepository.findOrderById(idOrder);
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> getOrder "+ e.getMessage());
			throw new OrderException("find order (where id) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> getOrder");
	    return order;
	}
	
	@RequestMapping(value="/listStatusOrder", method=RequestMethod.GET)
	public List<StatusOrder> getListaStatusOrder() {

		logger.info("START OrderController --> getListaStatusOrder");		
		List<StatusOrder> listStatusOrder = new ArrayList<StatusOrder>();
		try{
			listStatusOrder = statusOrderRepository.findAllStatusOrder();
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> getListaStatusOrder"+ e.getMessage());
			throw new OrderException("find order status order error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> getListaStatusOrder");		
	    return listStatusOrder;
	}	
	
	@RequestMapping(value="/findStatusOrderByIdStatusOrder/{idStatusOrder}", method=RequestMethod.GET)
	public StatusOrder getStatusOrderById(@PathVariable(value="idStatusOrder") String idStatusOrder) {

		logger.info("START OrderController --> getStatusOrderById");
		StatusOrder statusOrder = new StatusOrder();
		try{
			statusOrder = statusOrderRepository.findStatusOrderById(new Integer(idStatusOrder));			
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> getStatusOrderById"+ e.getMessage());
			throw new OrderException("find status order (where id) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> getStatusOrderById");		
	    return statusOrder;
	}	
	
	@RequestMapping(value="/findListOrder/{username}/{statusOrder}", method=RequestMethod.GET)
	public List<Order> getListOrderByUsernameAndIdStatus (@PathVariable(value="username") String username , @PathVariable(value="statusOrder") String statusOrder ) {

		logger.info("START OrderController --> getListOrderByUsernameAndIdStatus --> username: "+username );
		List<Order> listOrder = new ArrayList<Order>();
		List<Integer> listIdStatusOrder = new ArrayList<Integer>();
	 		
		if(statusOrder.equals(ORDER_ALL)){
			listIdStatusOrder.add(ID_STATUS_ORDER_OPEN);
			listIdStatusOrder.add(ID_STATUS_ORDER_SEND);
			listIdStatusOrder.add(ID_STATUS_ORDER_CLOSED);	
		}else if(statusOrder.equals(ORDER_CLOSED)){
			listIdStatusOrder.add(ID_STATUS_ORDER_CLOSED);
		}else if(statusOrder.equals(ORDER_ARCHIVED)){
			listIdStatusOrder.add(ID_STATUS_ORDER_ARCHIVED);
		}else if(statusOrder.equals(ORDER_CANCELLED)){
			listIdStatusOrder.add(ID_STATUS_ORDER_CANCELLED);
		} 
	 		
		try{
			listOrder = orderRepository.findAllByUserAndStatus(username, listIdStatusOrder);
		}catch(DataAccessException e){
			logger.info("ERROR  OrderController --> getListOrderByUsernameAndIdStatus"+ e.getMessage());
			throw new OrderException("find order (where userna,e) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END OrderController --> getListOrderByUsernameAndIdStatus --> username: "+username);
	    return listOrder;
	}
}