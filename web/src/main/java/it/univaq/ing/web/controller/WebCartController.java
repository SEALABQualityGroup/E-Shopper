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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.security.UserPrincipal;
import it.univaq.ing.security.model.Account;
import it.univaq.ing.security.model.Address;
import it.univaq.ing.security.model.GiftCard;
import it.univaq.ing.security.model.Payment;
import it.univaq.ing.web.model.CartItem;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.Order;
import it.univaq.ing.web.services.WebCartService;
import it.univaq.ing.web.services.WebCategoriesService;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebProductsService;

/**
 * 
 * @author LC
 *
 */
@Controller
@SessionAttributes(value={"accountName","categories","countCartItem","listCartItem","priceTotalWithEcoTax","priceTotal"})
public class WebCartController {
	
	protected Logger logger = Logger.getLogger(WebCartController.class.getName());

	@Autowired
	protected WebCartService cartService;
	
	@Autowired
	protected WebCategoriesService categoriesService;
	
	@Autowired
	protected WebProductsService productsService;
	
	@Autowired
	protected WebItemsService itemsService;
	
	public WebCartController(WebCartService cartService) {
		this.cartService = cartService;
	}
	
	@RequestMapping(value="/saveCartItemByIndex/{idItem}")
	public String saveCartItemByIndex(@PathVariable(value="idItem") Long idItem, Model model) {
		
		logger.info("START WebCartController --> saveCartItemByIndex");
		try{
			Item itemResponse = itemsService.findItemById(idItem);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				this.saveCartItem(accountSession, itemResponse, model);
				this.saveSubQuantityItem(itemResponse);
			}else{
				model.addAttribute("noLogin", Boolean.TRUE);
				Account account = new Account();
				model.addAttribute("account", account);
				return "login";	
			}	
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> saveCartItemByIndex: "+e.getMessage());
			return "redirect:/";
		}
		logger.info("END WebCartController --> saveCartItemByIndex");
		return "redirect:/";
	}
	
	@RequestMapping(value="/saveCartItemByOrder/{idItem}/{provenienza}/{statusOrder}")
	public String saveCartItemByOrder( Model model, @PathVariable(value="idItem") Long idItem, @PathVariable(value="provenienza") String provenienza, @PathVariable(value="statusOrder") String statusOrder ) {
		
		logger.info("START WebCartController --> saveCartItemByOrder");
		try{
			model.addAttribute("statusOrder", statusOrder);
			model.addAttribute("provenienza", provenienza);
			Item itemResponse = itemsService.findItemById(idItem);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			this.saveCartItem(accountSession, itemResponse, model);
			this.saveSubQuantityItem(itemResponse);
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> saveCartItemByOrder: "+e.getMessage());
			return "redirect:/viewOrder/{statusOrder}";
		}
		logger.info("END WebCartController --> saveCartItemByOrder");
		return "redirect:/viewOrder/{provenienza}/{statusOrder}";
	}
	
	@RequestMapping(value="/saveCartItemByProduct/{idItem}")
	public String saveCartItemByProduct(@PathVariable(value="idItem") Long idItem, Model model) {
		
		logger.info("START WebCartController --> saveCartItemByProduct");
		List<Item> items = new ArrayList<>();
		Long idProduct = 0l;
		Long idCategory = 0l;
		try{
			Item itemResponse = itemsService.findItemById(idItem);
			idCategory = itemResponse.getCategoryId();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				this.saveCartItem(accountSession, itemResponse, model);
				this.saveSubQuantityItem(itemResponse);
				if(itemResponse.getProductId() != null){
					items = itemsService.findItemsByIdProduct(itemResponse.getProductId());	
					idProduct = itemResponse.getProductId();
				}else{
					items = itemsService.findItemsByIdCategory(itemResponse.getCategoryId());
				}
				model.addAttribute("items", items);
				model.addAttribute("idProduct", idProduct);	
				model.addAttribute("idCategory", idCategory);
				model.addAttribute("path", "/shop");
			}else{
				model.addAttribute("noLogin", Boolean.TRUE);
				Account account = new Account();
				model.addAttribute("account", account);
				return "login";
			}
		}catch(RestClientException e) {
			logger.info("ERROR WebCartController --> saveCartItemByProduct: "+e.getMessage());
			return "redirect:/shop";
		}
		logger.info("END WebCartController --> saveCartItemByProduct");
		return "shop";
	}
	
	@RequestMapping(value="/saveCartItemByWishlist/{idItem}")
	public String saveCartItemByWishlist(@PathVariable(value="idItem") Long idItem, Model model) {
		
		logger.info("START WebCartController --> saveCartItemByWishlist");
		try{
			Item itemResponse = itemsService.findItemById(idItem);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				this.saveCartItem(accountSession, itemResponse, model);
				this.saveSubQuantityItem(itemResponse);
			}else{
				model.addAttribute("noLogin", Boolean.TRUE);
				Account account = new Account();
				model.addAttribute("account", account);
				return "login";
			}
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> saveCartItemByWishlist: "+e.getMessage());
			return "redirect:/findWishList";
		}
		logger.info("END WebCartController --> saveCartItemByWishlist");
		return "redirect:/findWishList";
	}
	
	@RequestMapping(value="/updateCartItem/{operazione}/{idCart}")
	public String updateCartItem(@PathVariable(value="operazione") String operazione, @PathVariable(value="idCart") Long idCart ) {
		
		logger.info("START WebCartController --> updateCartItem");
		Integer newQuantity = null;
		try{
			CartItem cartItem = cartService.findCartItemById(idCart);
			Item itemResponse = itemsService.findItemById(cartItem.getIdItem());
			Integer oldQuantity = cartItem.getQuantity();
			if(operazione.equals("add")){
				cartItem.setQuantity(oldQuantity + 1);
				cartItem.setPrice(cartItem.getPrice().add(itemResponse.getUnitCost()));
				cartService.updateCartItem(cartItem);
				this.saveSubQuantityItem(itemResponse);
			}else if(operazione.equals("delete")){
				newQuantity = oldQuantity - 1;
				if(newQuantity.equals(0)){
					cartService.deleteCartItemById(cartItem.getIdCart());		
				}else{
					cartItem.setQuantity(newQuantity);
					cartItem.setPrice(cartItem.getPrice().subtract(itemResponse.getUnitCost()));
					cartService.updateCartItem(cartItem);			
				}
				this.saveAddQuantityItem(itemResponse);
			}
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> updateCartItem: "+e.getMessage());
			return "redirect:/cart";
		}
		logger.info("END WebCartController --> updateCartItem");
		return "redirect:/cart";
	}
	
	@RequestMapping(value="/deleteCartItem/{idCartItem}")
	public String deleteCartItemById(@PathVariable(value="idCartItem") Long idCartItem) {
		
		logger.info("START WebCartController --> deleteCartItemById");
		try{
			CartItem cartItem = cartService.findCartItemById(idCartItem);
			Item itemResponse = itemsService.findItemById(cartItem.getIdItem());
			itemResponse.setTotQuantity(itemResponse.getTotQuantity() + cartItem.getQuantity());
			cartService.deleteCartItemById(idCartItem);	
			itemsService.updateItem(itemResponse);
			
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> deleteCartItemById: "+e.getMessage());
			return "index";
		}
		logger.info("END WebCartController --> deleteCartItemById");
		return "redirect:/cart";
	}
	
	@RequestMapping(value="/cart")
	public String cart(Model model){
		
		logger.info("START WebCartController --> cart");
		BigDecimal priceTotal = BigDecimal.ZERO;
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				List<CartItem> listCartItem =  cartService.findListCartItemByUsername(accountSession.getUsername());
					
				if(listCartItem.size() > 0){
					for(CartItem cartItem: listCartItem){	
						priceTotal = priceTotal.add(cartItem.getPrice());
						//In addition to the price you have to cycle to set the item - cart association
						Item itemResponse = itemsService.findItemById(cartItem.getIdItem());
						cartItem.setItem(itemResponse);	
					}	

					
					if(accountSession.getListGiftCard().size() == 0){
						model.addAttribute("noGiftCard", Boolean.TRUE);
					}else{
						model.addAttribute("noGiftCard", Boolean.FALSE);
						List<GiftCard> allGiftCard = new ArrayList<GiftCard>();
						for(GiftCard gf : accountSession.getListGiftCard()){
							if(!(gf.getBalanceAvailabled().compareTo(BigDecimal.ZERO) == 0)){
								allGiftCard.add(gf);
							}
						}
						if(allGiftCard.size() == 0){
							model.addAttribute("noGiftCard", Boolean.TRUE);
						}
						model.addAttribute("allGiftCard", allGiftCard);

					}
					model.addAttribute("noCart", Boolean.FALSE);
				}else{
					model.addAttribute("noCart", Boolean.TRUE);

				}
				this.countCartItem(accountSession.getUsername(), model);
				GiftCard giftCard = new GiftCard();
				giftCard.setIdGift(0l);
				model.addAttribute("priceTotal", priceTotal);
				model.addAttribute("priceTotalWithEcoTax", priceTotal.add(new BigDecimal(2)));
				model.addAttribute("listCartItem", listCartItem);
				model.addAttribute("giftCard", giftCard);
			}
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> cart: "+e.getMessage());
			return "index";
		}
		model.addAttribute("path", "/cart");
		logger.info("END WebCartController --> cart");
		return "cart";	
	}	
	
	@RequestMapping(value="/checkout")
	public String checkout(Model model,  @ModelAttribute("giftCard") GiftCard giftCardS){
		
		logger.info("START WebCartController --> checkout");
		Order order = new Order();
		BigDecimal priceGiftCard = BigDecimal.ZERO;
		BigDecimal priceFinalTotal = BigDecimal.ZERO;
		Boolean noGiftCard = Boolean.TRUE;
		Boolean noAddress = Boolean.TRUE;
		Boolean noPayment = Boolean.TRUE;
		Boolean noPlaceOrder = Boolean.FALSE;	
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			
			if(giftCardS.getIdGift() != null){
				noGiftCard = Boolean.FALSE;
				for(GiftCard giftCard : accountSession.getListGiftCard()){
					if(giftCard.getIdGift().equals(giftCardS.getIdGift())){
						priceGiftCard = giftCard.getBalanceAvailabled();
						BigDecimal priceTotalWithEcoTax =  (BigDecimal) model.asMap().get("priceTotalWithEcoTax");
						if(priceGiftCard.compareTo(priceTotalWithEcoTax) == -1){
							priceFinalTotal = priceTotalWithEcoTax.subtract(priceGiftCard);
						}else{
							priceGiftCard = priceTotalWithEcoTax;
							priceFinalTotal = new BigDecimal("0.00");
						}
					}
				}	
			}else{
				giftCardS.setIdGift(0l);
			}
			// Lista Address --> variabile per definire se è vuota oppure no
			if (accountSession.getListAddress().size() > 0){
				for(Address address:accountSession.getListAddress()){
					if(address.getFlagActive().equals("Y")){
						noAddress = Boolean.FALSE;
						break;
					}
				}
			}
			// Lista Payment --> variabile per definire se è vuota oppure no
			if (accountSession.getListPayment().size() > 0){			
				for(Payment payment : accountSession.getListPayment()){
					if(payment.getFlagActive().equals("Y") && !payment.getCreditCardExpire()){
						noPayment = Boolean.FALSE;
						break;
					}
				}
			}
			if(noAddress || noPayment ){
				noPlaceOrder = Boolean.TRUE;	
			}
			
			model.addAttribute("noPlaceOrder", noPlaceOrder);
			model.addAttribute("noAddress", noAddress);
			model.addAttribute("noPayment", noPayment);
			model.addAttribute("order", order);
			model.addAttribute("provenienza","cart");
			model.addAttribute("priceGiftCard",priceGiftCard);
			model.addAttribute("priceFinalTotal",priceFinalTotal);
			model.addAttribute("noGiftCard",noGiftCard);
			model.addAttribute("idGiftCard", giftCardS.getIdGift());
			model.addAttribute("account", accountSession);
		}catch (RestClientException e) {
			logger.info("ERROR WebCartController --> checkout: "+e.getMessage());
			return "index";
		}
		model.addAttribute("path", "/cart");
		logger.info("END WebCartController --> checkout");
		return "checkout";	
	}
	
	private void saveCartItem(Account accountSession, Item itemResponse, Model model){

		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    CartItem cartItem = new CartItem();
		cartItem = cartService.findCartByUsernameByIdItem(accountSession.getUsername(), itemResponse.getItemId());
		if(cartItem == null || cartItem.getIdCart().equals(0l)){
			cartItem = new CartItem();
			cartItem.setIdAccount(accountSession.getUsername());			
			cartItem.setIdItem(itemResponse.getItemId());
			cartItem.setPrice(itemResponse.getUnitCost());
			cartItem.setQuantity(1);		
		    cartItem.setInsertDate(sqldate);
		}else{
			cartItem.setQuantity(cartItem.getQuantity() +1);
			cartItem.setPrice(cartItem.getPrice().add(itemResponse.getUnitCost()));
		}
	    cartItem.setModifyDate(sqldate);		
		
	    CartItem cartItemResponse = cartService.saveCartItem(cartItem);
		if(cartItemResponse == null){
			logger.info("ERRORE NEL SAVE CART --- saveCartItemByIndex ---- ");
			model.addAttribute("erroreSaveCart", Boolean.TRUE);				
		}
		this.countCartItem(accountSession.getUsername(), model);
	}
	
	private void countCartItem(String username, Model model){
		Integer countCartItem = cartService.countCartItemByUsername(username);
		model.addAttribute("countCartItem", countCartItem);
	}
	
	private void saveSubQuantityItem (Item item){
		item.setTotQuantity(item.getTotQuantity() - 1);
		itemsService.updateItem(item);
	}
	
	private void saveAddQuantityItem (Item item){
		item.setTotQuantity(item.getTotQuantity() + 1);
		itemsService.updateItem(item);
	}
}
