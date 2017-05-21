package it.univaq.ing.web.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import it.univaq.ing.security.UserPrincipal;
import it.univaq.ing.security.model.Account;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.WishList;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebWishListService;

/**
 * 
 * @author LC
 *
 */
@Controller
@SessionAttributes(value={"accountName","categories","countCartItem", "listCartItem"})
public class WebWishListController {
	
	protected Logger logger = Logger.getLogger(WebWishListController.class.getName());

	@Autowired
	protected WebWishListService wishListService;
	
	@Autowired
	protected WebItemsService itemsService;
	
	public WebWishListController(WebWishListService wishListService) {
		this.wishListService = wishListService;
	}
	
	@RequestMapping(value="/saveWishList/{idItem}")
	public String saveWishListByIndex(@PathVariable(value="idItem") Long idItem, Model model) {
		
		logger.info("START WebWishListController --> saveWishListByIndex");
		try{
			//find item
			Item itemResponse = itemsService.findItemById(idItem);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				this.saveWishList(accountSession, itemResponse, model);
			}else{
				model.addAttribute("noLogin", Boolean.TRUE);
				Account account = new Account();
				model.addAttribute("account", account);
				return "login";
			}				
		}catch (RestClientException e) {
			logger.info("ERROR WebWishListController --> saveWishListByIndex: "+e.getMessage());
			return "redirect:/";
		}
		logger.info("END WebWishListController --> saveWishListByIndex");	
		return "redirect:/";
	}
	
	@RequestMapping(value="/saveWishListByProduct/{idItem}")
	public String saveWishListByProduct(@PathVariable(value="idItem") Long idItem, Model model) {
		
		logger.info("START WebWishListController --> saveWishListByProduct");
		List<Item> items = new ArrayList<>();
		Long idProduct = 0l;
		Long idCategory = 0l;
		try{
			Item itemResponse = itemsService.findItemById(idItem);
			idCategory = itemResponse.getCategoryId();

			if(itemResponse.getProductId() != null){
				items = itemsService.findItemsByIdProduct(itemResponse.getProductId());	
				idProduct = itemResponse.getProductId();
			}else{
				items = itemsService.findItemsByIdCategory(itemResponse.getCategoryId());
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				this.saveWishList(accountSession, itemResponse, model);
				model.addAttribute("idProduct", idProduct);	
				model.addAttribute("idCategory", idCategory);	
				model.addAttribute("items", items);	
				model.addAttribute("path", "/shop");
				
			}else{
				model.addAttribute("noLogin", Boolean.TRUE);
				Account account = new Account();
				model.addAttribute("account", account);
				return "login";
			}	
		}catch (RestClientException e) {
			logger.info("ERROR WebWishListController --> saveWishListByProduct: "+e.getMessage());
			return "redirect:/shop";
		}
		logger.info("END WebWishListController --> saveWishListByProduct");
		return "shop";							
	}
	
	@RequestMapping(value="/findWishList")
	public String findWishList(Model model) {
		
		logger.info("START WebWishListController --> findWishList");
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			List<WishList> listWishList =  wishListService.findWishListByUsername(accountSession.getUsername());
			if(listWishList.size() > 0){
				for(WishList wishList : listWishList){
					wishList.setItem(itemsService.findItemById(wishList.getIdItem()));
				}
				model.addAttribute("listWishList", listWishList);
				model.addAttribute("noWishList", Boolean.FALSE);
			}else{
				model.addAttribute("noWishList", Boolean.TRUE);
			}	
		}catch (RestClientException e) {
			logger.info("ERROR WebWishListController --> findWishList: "+e.getMessage());
			return "index";
		}
		logger.info("END WebWishListController --> findWishList");
		model.addAttribute("path", "/wishlist");
		return "wishlist";
	}	
	
	@RequestMapping(value="/deleteWishListById/{idWishList}")
	public String deleteWishListById(@PathVariable(value="idWishList") Long idWishList) {
		
		logger.info("START WebWishListController --> deleteWishListById");
		try{
			wishListService.deleteWishListById(idWishList); 	
		}catch (RestClientException e) {
			logger.info("ERROR WebWishListController --> deleteWishListById: "+e.getMessage());
			return "index";
		}
		logger.info("END WebWishListController --> deleteWishListById");
		return "redirect:/findWishList";
	}
	
	private void saveWishList(Account accountSession, Item itemResponse, Model model){

		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    
	    WishList wishListResponse = wishListService.findWishListByUsernameByIdItem(accountSession.getUsername(), itemResponse.getItemId());
		if(wishListResponse == null){
			WishList wishList = new WishList();
			wishList.setIdAccount(accountSession.getUsername());			
			wishList.setIdItem(itemResponse.getItemId());
			wishList.setInsertDate(sqldate);
			wishList.setModifyDate(sqldate);
			WishList wishListResponseSave = wishListService.saveWishList(wishList);
			if(wishListResponseSave == null){
				logger.info("ERROR WebWishListController --> saveWishList");
				model.addAttribute("erroreSavewishList", Boolean.TRUE);				
			}
		}  
	}
}
