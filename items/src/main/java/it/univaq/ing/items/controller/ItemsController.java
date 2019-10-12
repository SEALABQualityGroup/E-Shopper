package it.univaq.ing.items.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.items.ItemException;
import it.univaq.ing.items.domain.Item;
import it.univaq.ing.items.repository.ItemRepository;
import it.univaq.ing.items.util.Experiment;
import it.univaq.ing.items.util.SyntheticNoise;;

/**
 * 
 * @author LC
 */
@RestController
public class ItemsController {
	@Autowired Tracer tracer;
	
	@Value("#{'${experiment.findItemsRandomByIdProduct}'.split(',')}")
	List<String> findItemsRandomByIdProductLatencyInjections;

	@Value("#{'${experiment.findItemRandom}'.split(',')}")
	List<String> findItemRandomLatencyInjections;

	@Value("#{'${experiment.findFeaturesItemRandom}'.split(',')}")
	List<String> findFeaturesItemRandomLatencyInjections;

	@Value("#{'${noise.findItemsRandomByIdProduct}'.split(',')}")
	List<String> noiseFindItemsRandomByIdProduct;

	@Value("#{'${noise.findItemRandom}'.split(',')}")
	List<String> noiseFindItemRandom;

	@Value("#{'${noise.findFeaturesItemRandom}'.split(',')}")
	List<String> noiseFindFeaturesItemRandom;

	
	protected Logger logger = Logger.getLogger(ItemsController.class.getName());

	protected ItemRepository itemRepository;

	@Autowired
	public ItemsController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping("/findItems/{idProduct}")
	public List<Item> getItemsByIdProduct(@PathVariable(value="idProduct") Long idProduct) {
		
		logger.info("START ItemsController --> getItemsByIdProduct");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.getItemsByIdProduct(idProduct);	
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController--> getItemsByIdProduct: "+ e.getMessage());
			throw new ItemException("find item (where product) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> getItemsByIdProduct");
	    return items;
	}
	
	@RequestMapping("/findItemsByIdCategory/{idCategory}")
	public List<Item> getItemsByIdCategory(@PathVariable(value="idCategory") Long idCategory) {
		
		logger.info("START ItemsController --> getItemsByIdCategory");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.getItemsByIdCategory(idCategory);
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController--> getItemsByIdCategory: "+ e.getMessage());
			throw new ItemException("find item (where category) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> getItemsByIdCategory");
		return items;
	}
	
	@RequestMapping("/findItemsRandomByProductId/{idProduct}")
	public List<Item> findItemsRandomByIdProduct(@PathVariable(value="idProduct") Long idProduct) {
		Span span = tracer.getCurrentSpan();
		Experiment.injectLatency(span, findItemsRandomByIdProductLatencyInjections);
		SyntheticNoise.injectLatency(span, noiseFindItemsRandomByIdProduct);

		logger.info("START ItemsController --> findItemsRandomByIdProduct");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.findItemsRandomByIdProduct(idProduct);
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController--> findItemsRandomByIdProduct: "+ e.getMessage());
			throw new ItemException("find item random error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> findItemsRandomByIdProduct");
		return items;
	}
	
	@RequestMapping("/findItem/{idItem}")
	public Item getItemById(@PathVariable(value="idItem") Long idItem) {
		
		logger.info("START ItemsController --> getItemById");
		Item item = new Item();
		try{
			item = itemRepository.getItemById(idItem);
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController--> getItemById: "+ e.getMessage());
			throw new ItemException("find item (where id) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> getItemById");
	    return item;
	}
	
	@RequestMapping("/findItemByProductFilter/{idProduct}/{priceMin}/{priceMax}")
	public List<Item> findItemByProductFilter(@PathVariable(value="idProduct") Long idProduct, @PathVariable(value="priceMin") Long priceMin, @PathVariable(value="priceMax") Long priceMax) {
		
		logger.info("START ItemsController --> findItemByProductFilter");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.findItemByProductFilter(idProduct, priceMin, priceMax);
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController --> findItemByProductFilter: "+ e.getMessage());
			throw new ItemException("find item (where filter) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> findItemByProductFilter");
	    return items;	
	}
	
	@RequestMapping("/findItemByCategoryFilter/{idCategory}/{priceMin}/{priceMax}")
	public List<Item> findItemByCategoryFilter(@PathVariable(value="idCategory") Long idCategory, @PathVariable(value="priceMin") Long priceMin, @PathVariable(value="priceMax") Long priceMax) {
		
		logger.info("START ItemsController --> findItemByCategoryFilter");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.findItemByCategoryFilter(idCategory, priceMin, priceMax);
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController --> findItemByCategoryFilter: "+ e.getMessage());
			throw new ItemException("find item (where filter) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> findItemByCategoryFilter");
	    return items;	
	}
	
	@RequestMapping("/findItemsRandom")
	public List<Item> findItemRandom() {
		Span span = tracer.getCurrentSpan();
		Experiment.injectLatency(span, findItemRandomLatencyInjections);
		SyntheticNoise.injectLatency(span, noiseFindItemRandom);

		logger.info("START ItemsController --> findItemRandom");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.findItemRandom();
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController --> findItemRandom: "+ e.getMessage());
			throw new ItemException("find item (radom item) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> findItemRandom");
	    return items;
	}
	
	@RequestMapping("/findFeaturesItemRandom")
	public List<Item> findFeaturesItemRandom() {
		Span span = tracer.getCurrentSpan();
		Experiment.injectLatency(span, findFeaturesItemRandomLatencyInjections);
		SyntheticNoise.injectLatency(span, noiseFindFeaturesItemRandom);
		
		logger.info("START ItemsController --> findFeaturesItemRandom");
		List<Item> items = new ArrayList<Item>();
		try{
			items = itemRepository.findFeaturesItemRandom();
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController --> findFeaturesItemRandom: "+ e.getMessage());
			throw new ItemException("find item features item random error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ItemsController --> findFeaturesItemRandom");
	    return items;
	}
	
	@RequestMapping(value="/updateItem", method = RequestMethod.POST)
	public Item updateItem(@RequestBody Item item){
		logger.info("START ItemsController --> updateItem");	
		try{
			logger.info("END ItemsController --> updateItem");
			return itemRepository.save(item);
		
		}catch(DataAccessException e){
			logger.info("ERROR  ItemsController --> updateItem"+ e.getMessage());
			throw new ItemException("updare item error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
	}
}
