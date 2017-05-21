package it.univaq.ing.wishlist.controller;

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
import it.univaq.ing.wishlist.WishListException;
import it.univaq.ing.wishlist.domain.WishList;
import it.univaq.ing.wishlist.repository.WishListRepository;

/**
 * 
 * @author LC
 */
@RestController
public class WishListController {
	
	protected Logger logger = Logger.getLogger(WishListController.class.getName());
	protected WishListRepository wishListRepository;
	
	@Autowired
	public WishListController(WishListRepository wishListRepository) {
		this.wishListRepository = wishListRepository;
	}

	@RequestMapping(value="/saveWishList", method=RequestMethod.POST)
	public WishList saveWishList(@RequestBody WishList wishList) {
		logger.info("START WishListController --> saveWishList");
		WishList wishListResp = new WishList();
		try{
			wishListResp = wishListRepository.save(wishList);
		}catch(DataAccessException e){
			logger.info("ERROR  WishListController --> saveWishList: "+ e.getMessage());
			throw new WishListException("save wish list error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END WishListController --> saveWishList");
		return wishListResp;
	}
	
	@RequestMapping(value="/findWishList/{username:.+}", method=RequestMethod.GET)
	public List<WishList> findWishListByUsername(@PathVariable(value="username") String username) {
		
		logger.info("START WishListController --> findWishListByUsername --> username: "+username);
		List<WishList> listWishList = new ArrayList<WishList>();		
		try{
			listWishList = wishListRepository.findWishListByUsername(username);
		}catch(DataAccessException e){
			logger.info("ERROR  WishListController --> findWishListByUsername: "+ e.getMessage());
			throw new WishListException("find wishList (where username) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END WishListController --> findWishListByUsername --> username: "+username);
		return listWishList;
	}
	
	@RequestMapping(value="/findWishListByUsernameByIdItem/{username:.+}/{idItem}", method=RequestMethod.GET)
	public WishList findWishListByUsernameByIdItem(@PathVariable(value="username") String username, @PathVariable(value="idItem") String idItem) {
		logger.info("START WishListController --> findWishListByUsernameByIdItem --> username: "+username);
		WishList wishList = new WishList();
		try{
			wishList = wishListRepository.findWishListByUsernameByIdItem(username,Long.valueOf(idItem));			
		}catch(DataAccessException e){
			logger.info("ERROR  WishListController --> findWishListByUsernameByIdItem: "+ e.getMessage());
			throw new WishListException("find wishList (where username and idItem) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END WishListController --> findWishListByUsernameByIdItem --> username: "+username);
		return wishList;
	}
	
	@RequestMapping(value="/deleteWishListById/{idWishList}", method=RequestMethod.DELETE)
	public void deleteWishListById(@PathVariable(value="idWishList") String idWishList) {
	   logger.info("START WishListController --> deleteWishListById");
	   try{
		   wishListRepository.deleteWishListById(Long.valueOf(idWishList));
	   }catch(DataAccessException e){
			logger.info("ERROR  WishListController --> deleteWishListById"+ e.getMessage());
			throw new WishListException("delete wishList error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
	}	
}