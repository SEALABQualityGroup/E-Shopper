package it.univaq.ing.cart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.cart.CartException;
import it.univaq.ing.cart.domain.CartItem;
import it.univaq.ing.cart.repository.CartRepository;

/**
 * 
 * @author LC
 */
@RestController
public class CartController {
	
	protected Logger logger = Logger.getLogger(CartController.class.getName());

	protected CartRepository cartRepository;

	@Autowired
	public CartController(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}
	
	@RequestMapping(value="/saveCartItem", method=RequestMethod.POST)
	public CartItem saveCartItem(@RequestBody CartItem cartItem) {
		
		logger.info("START CartController --> saveCartItem");
		CartItem cartItemResp = new CartItem();
		try{
			cartItemResp = cartRepository.save(cartItem);
		}catch(DataAccessException e){
			logger.info("ERROR  CartController --> saveCartItem: "+ e.getMessage());
			throw new CartException("save cartItem error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END CartController --> saveCartItem");
		return cartItemResp;
	}
	
	@RequestMapping(value="/updateListCartItem", method=RequestMethod.POST)
	public void updateListCartItem(@RequestBody String username) {
        
    	logger.info("START CartController --> updateListCartItem --> username: "+username);
		try{
        	cartRepository.updateListCartItem("S",username);
        }catch (CartException e) {
			logger.info("ERROR CartController --> updateListCartItem: "+e.getMessage());
			throw new CartException("update cartItem (where username) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
        logger.info("END CartController --> updateListCartItem --> username: "+username);
	}
	
	
	@RequestMapping(value="/updateCartItem", method=RequestMethod.PUT)
	public CartItem updateCartItem(@RequestBody CartItem cartItem) {
        
		logger.info("START CartController --> updateListCartItem");
		CartItem cartItemResp = new CartItem();
		 try{
			 cartItemResp = cartRepository.save(cartItem);
		 }catch (CartException e) {
			logger.info("ERROR CartController --> updateListCartItem: "+e.getMessage());
			throw new CartException("update cartItem error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
	    logger.info("END CartController --> updateListCartItem");
		return cartItemResp;
	}
	
	@RequestMapping(value="/findCartItem/{username:.+}", method=RequestMethod.GET)
	public List<CartItem> findListCartItemByUsername(@PathVariable(value="username") String username) {
		
		logger.info("START CartController --> findListCartItemByUsername --> username: "+username);
		List<CartItem> listCartItem = new ArrayList<CartItem>();
		 try{
			 listCartItem = cartRepository.findCartByUsername(username);
		 }catch (CartException e) {
			logger.info("ERROR CartController --> findListCartItemByUsername "+e.getMessage());
			throw new CartException("find cartItem (where username) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
		 logger.info("END CartController --> findListCartItemByUsername --> username: "+username);
		return listCartItem;
	}
	
	
	@RequestMapping(value="/findCartByUsernameByIdItem/{username:.+}/{idItem}", method=RequestMethod.GET)
	public CartItem findCartByUsernameByIdItem(@PathVariable(value="username") String username, @PathVariable(value="idItem") String idItem) {
		
		logger.info("START CartController --> findCartByUsernameByIdItem --> username: "+username);
		CartItem cartItem = new CartItem();
		try{
			cartItem = cartRepository.findCartByUsernameByIdItem(username,Long.valueOf(idItem));
		}catch (CartException e) {
			logger.info("ERROR CartController --> findCartByUsernameByIdItem "+e.getMessage());
			throw new CartException("find cartItem (where username AND idItem) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
		logger.info("END CartController --> findCartByUsernameByIdItem --> username: "+username);
		return cartItem;
	}
	
	@RequestMapping(value="/countCartItem/{username:.+}")
	public Integer countCartItemByUsername(@PathVariable(value="username") String username) {
		
		logger.info("START CartController --> countCartItemByUsername --> username: "+username);
        try{
        	return cartRepository.countCartByUsername(username);
        }catch (CartException e) {
			logger.info("ERROR CartController --> countCartItemByUsername: "+e.getMessage());
			throw new CartException("select cartItem (count) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}		
	}
	
	@RequestMapping(value="/deleteCartItemByIdCartItem/{idCartItem}", method=RequestMethod.DELETE)
	public void deleteCartItemById(@PathVariable(value="idCartItem") String idCartItem) {
		
		logger.info("START CartController --> deleteCartItemById");
	    try{
			cartRepository.deleteCartItemById(Long.valueOf(idCartItem));
	    }catch(CartException e){
			logger.info("ERROR CartController --> deleteCartItemById: "+e.getMessage());
			throw new CartException("delete cartItem by id error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
		logger.info("END CartController --> deleteCartItemById");
	}
	
	@RequestMapping(value="/deleteCartItemByUsername/{idAccount}", method=RequestMethod.DELETE)
	public void deleteCartItemByUsername(@PathVariable(value="idAccount") String idAccount) {
	   
		logger.info("START CartController --> deleteCartItemByUsername --> username: "+idAccount);
	   try{
			cartRepository.deleteListCartItem(idAccount);
	   }catch(CartException e){
			logger.info("ERROR CartController --> deleteCartItemByUsername: "+e.getMessage());
			throw new CartException("delete cartItem by username error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
	   logger.info("END CartController --> deleteCartItemByUsername --> username: "+idAccount);
	}
	
	@RequestMapping(value="/findCartItemById/{idCart}", method=RequestMethod.GET)
	public CartItem findCartItemById(@PathVariable(value="idCart") Long idCart) {
		
		logger.info("START CartController --> findCartItemById");
		CartItem cartItem = new CartItem();
		try{
			cartItem = cartRepository.findCartItemById(idCart);
		}catch(CartException e){
			logger.info("ERROR CartController --> findCartItemById: "+e.getMessage());
			throw new CartException("find cartItem by idCart error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END CartController --> findCartItemById");
		return cartItem;
	}
}
