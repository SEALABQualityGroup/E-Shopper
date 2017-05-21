package it.univaq.ing.web.controller;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.univaq.ing.security.model.Account;
import it.univaq.ing.security.model.Address;
import it.univaq.ing.security.model.GiftCard;
import it.univaq.ing.security.model.Payment;
import it.univaq.ing.web.model.CartItem;
import it.univaq.ing.web.model.Order;
import it.univaq.ing.web.model.RelCartOrder;
import it.univaq.ing.web.model.StatusOrder;
import it.univaq.ing.web.services.WebAccountsService;
import it.univaq.ing.web.services.WebCartService;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebOrderService;


/**
 * Client controller, fetches Order info from the microservice via
 * {@link WebOrderService}.
 * 
 * @author LC
 */
@Controller	
@SessionAttributes(value={"accountName","listCartItem","priceTotalWithEcoTax","countCartItem","priceTotal"})

public class WebOrderController {

	protected Logger logger = Logger.getLogger(WebOrderController.class.getName());
	private static final Long ID_STATUS_ORDER_OPEN =1l; 
	private static final Long ID_STATUS_ORDER_CANCELLED =3l;
	private static final Long ID_STATUS_ORDER_ARCHIVED =4l;

	@Autowired
	protected WebOrderService orderService;
	
	@Autowired
	protected WebAccountsService accountsService;
	
	@Autowired
	protected WebCartService cartService;
	
	
	@Autowired
	protected WebItemsService itemService;
	
	public WebOrderController(WebOrderService orderService) {
		this.orderService = orderService;
	}
	
	// ----------------------------------------------------------------
	// ---------------------SAVE ORDER-- @PathVariable(value="idGiftCard") Long idGiftCard,-------------------------------
	// ----------------------------------------------------------------

	@RequestMapping(value="/saveOrder/{idGiftCard}")
	public String saveOrder(Model model, @ModelAttribute("order") Order order, @RequestParam("idAddress") Long idAddress, @PathVariable(value="idGiftCard") Long idGiftCard,  @RequestParam("idPayment") Long idPayment){
		
		logger.info("START WebOrderController --> saveOrder");
		List<RelCartOrder> listaRelCartOrder = new ArrayList<RelCartOrder>();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			@SuppressWarnings("unchecked")
			List<CartItem> listCartItem = (List<CartItem>) model.asMap().get("listCartItem");
			BigDecimal priceTotalWithEcoTax = (BigDecimal) model.asMap().get("priceTotalWithEcoTax");

			order.setIdAddress(idAddress);
			order.setIdPayment(idPayment);	
			if(idGiftCard != 0){
				for(GiftCard giftCard: accountSession.getListGiftCard()){
					if(giftCard.getIdGift().equals(idGiftCard)){
						order.setIdGift(idGiftCard);				
						//UPDATE GIFT_CART
						giftCard = this.updateGiftCart(giftCard, sqldate, priceTotalWithEcoTax);
					}
				}
			}
			order.setIdStatus(orderService.findStatusOrderById(ID_STATUS_ORDER_OPEN));	
			order.setTotPrice(priceTotalWithEcoTax);
			order.setIdAccount(accountSession.getEmail());
			order.setInsertDate(sqldate);
			order.setModifyDate(sqldate);
		
			for (CartItem cartItem: listCartItem){
				RelCartOrder relCartOrder = new  RelCartOrder();				
				relCartOrder.setIdCart(cartItem.getIdCart());			
				relCartOrder.setInsertDate(sqldate);
				relCartOrder.setModifyDate(sqldate);
				listaRelCartOrder.add(relCartOrder);
			}
			order.setListRelCartOrder(listaRelCartOrder);
			Order orderResponse = orderService.saveOrder(order);				
			//  delete association cart-user
			cartService.updateListCartItem(accountSession.getUsername());
			// save account --> update GiftCard
			Account accountResponse = accountsService.updateAccount(accountSession);
			accountSession.setListGiftCard(accountResponse.getListGiftCard());
			model.addAttribute("account", accountResponse);
			model.addAttribute("countCartItem", new Integer(0));
			orderResponse.setAddressSmall(this.calculateStringAddressSmall(accountResponse, orderResponse.getIdAddress()));
			model.addAttribute("order", orderResponse);
		}catch(RuntimeException e){
			model.addAttribute("orderError", Boolean.TRUE );
			logger.info("ERROR WebOrderController --> saveOrder: "+e.getMessage());
			return "redirect:/checkout";
		}	
		logger.info("END WebOrderController --> saveOrder");	
		return "successOrder";
	}

	
	//  ----------------------------------------------------------------
	//  -------------------------ORDERS---------------------------------
	//  ----------------------------------------------------------------
	
	@RequestMapping(value="/viewOrder/{provenienza}/{statusOrder}", method=RequestMethod.GET)
	public String viewOrder(Model model,@PathVariable String  provenienza, @PathVariable String  statusOrder){
		
		logger.info("START WebOrderController --> viewOrderOpen");		
 		try{
 			this.getListOrder(model, statusOrder);
 		}catch(RuntimeException e){
			model.addAttribute("orderError", Boolean.TRUE );
			logger.info("ERROR WebOrderController --> viewOrderOpen: "+e.getMessage());
			return "redirect:/manageAccount";
		}

		logger.info("END WebOrderController --> viewOrderOpen");
		if(provenienza.equals("o")){
			model.addAttribute("path", "/order");
		}else{
			model.addAttribute("path", "/account");
		}
		model.addAttribute("provenienza", provenienza);
		return "viewOrder";	
	}
		
	@RequestMapping(value="/backCheckup")
	public String backCheckup(Model model){		
		logger.info("START WebOrderController --> backCheckup");
		return "redirect:/cart";	
	}	
	
	@RequestMapping(value="/orderCancelled/{provenienza}/{id}")
	public String orderCancelled(Model model,  @PathVariable(value="provenienza") String  provenienza, @PathVariable(value="id") String  id){		
		logger.info("START WebOrderController --> orderCancelled");
		model.addAttribute("provenienza", provenienza);
		try{
			this.updateOrder(new Long(id), ID_STATUS_ORDER_CANCELLED);
			this.getListOrder(model, "all");
		}catch(RuntimeException e){
			model.addAttribute("orderError", Boolean.TRUE );
			logger.info("ERROR WebOrderController --> orderCancelled: "+e.getMessage());
			return "redirect:/viewOrder/all";
		}
		model.addAttribute("saveCancelled", Boolean.TRUE);
		if(provenienza.equals("o")){
			model.addAttribute("path", "/order");
		}else{
			model.addAttribute("path", "/account");
		}
		
		logger.info("END WebOrderController --> orderCancelled");
		return "viewOrder";	
	}	
	
	@RequestMapping(value="/orderArchived/{id}/{statusOrder}")
	public String orderArchived(Model model,@PathVariable(value="statusOrder") String  statusOrder, @PathVariable(value="id") String  id){		
		logger.info("START WebOrderController --> orderArchived");
		model.addAttribute("statusOrder", statusOrder);
		try{
			this.updateOrder(new Long(id), ID_STATUS_ORDER_ARCHIVED);
			this.getListOrder(model, statusOrder);
			model.addAttribute("path", "/account");
		}catch(RuntimeException e){
			model.addAttribute("orderError", Boolean.TRUE );
			logger.info("ERROR WebOrderController --> orderArchived: "+e.getMessage());
			return "redirect:/viewOrder/{statusOrder}";
		}
		model.addAttribute("saveArchived", Boolean.TRUE);
		logger.info("END WebOrderController --> orderArchived");
		return "viewOrder";	
	}	
	
	private void updateOrder(Long idOrder, Long idStatusOrder){
		Order order = orderService.findOrder(idOrder);
		StatusOrder statusOrder = orderService.findStatusOrderById(idStatusOrder);
		order.setIdStatus(statusOrder);
		orderService.updateOrder(order);
		
	}

	private GiftCard updateGiftCart(GiftCard giftCard, Date sqldate, BigDecimal priceTotalWithEcoTax){
		
		if(giftCard.getBalanceAvailabled().compareTo(priceTotalWithEcoTax) == 1){
			giftCard.setBalanceUsed(giftCard.getBalanceUsed().add(priceTotalWithEcoTax));
			giftCard.setBalanceAvailabled(giftCard.getBalanceTot().subtract(giftCard.getBalanceUsed()));
		}else{
			giftCard.setBalanceAvailabled(BigDecimal.ZERO);
			giftCard.setBalanceUsed(giftCard.getBalanceTot());
		}
		giftCard.setModifyDate(sqldate);
		return giftCard;
	}
	
	private List<Order> getListOrderCustom (List<Order> listOrder, Account accountSession){
		
		for(Order order : listOrder){
			for(Address address: accountSession.getListAddress()){
				if(address.getIdAddress().equals(order.getIdAddress())){
					order.setAddressSmall(address.getFullName()+ " " +address.getAddress1() + " " +address.getCity());
				}
			}		
			if(order.getIdGift() != null){
				for(GiftCard giftCard : accountSession.getListGiftCard()){
					if(giftCard.getIdGift().equals(order.getIdGift())){
						order.setCodeGiftCard(giftCard.getCode());
					}
				}
			}	
			for(Payment payment: accountSession.getListPayment()){
				if(payment.getIdPayment().equals(order.getIdPayment())){
					order.setCodePayment("***" + payment.getNumber().substring(4));
				}
			}				
			for (RelCartOrder relCartOrder : order.getListRelCartOrder()){
				CartItem cartItem = cartService.findCartItemById(relCartOrder.getIdCart());
				cartItem.setItem(itemService.findItemById(cartItem.getIdItem()));
				relCartOrder.setCart(cartItem);
			}
		}
		return listOrder;
	}
	
	private String calculateStringAddressSmall(Account accountResponse, Long idAddress ){
		String smallAddress = "";
		for(Address address: accountResponse.getListAddress()){
			if(address.getIdAddress().equals(idAddress)){
				smallAddress = address.getFullName()+", "+address.getAddress1()+" "+address.getCity();
				break;
			}
		}
		return smallAddress;
	}
	
	private void getListOrder(Model model, String statusOrder){
			Boolean noListOrder = Boolean.FALSE;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
					
	 		List<Order> orderList = orderService.findListOrder(accountSession.getUsername(), statusOrder);
	 		if(orderList.size() > 0){
	 	 		orderList = this.getListOrderCustom(orderList, accountSession);	 			
	 			model.addAttribute("listOrder", orderList);
	 		}else{
	 			noListOrder = Boolean.TRUE;
	 		}
	 		model.addAttribute("noListOrder", noListOrder);
		 	model.addAttribute("statusOrder", statusOrder);
	}
 }