package it.univaq.ing.products.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.products.ProductException;
import it.univaq.ing.products.domain.Product;
import it.univaq.ing.products.repository.ProductRepository;

/**
 * 
 * @author LC
 *
 */
@RestController
public class ProductsController {
	
	protected Logger logger = Logger.getLogger(ProductsController.class.getName());

	protected ProductRepository productRepository;
	protected Random randomGenerator = new Random();

	@Autowired
	public ProductsController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@RequestMapping("/findProduct/{categoryId}")
	public List<Product> findProductByCategory(@PathVariable(value="categoryId") Long categoryId) {
		
		logger.info("START ProductsController --> findProductByCategory");
		List<Product> products = new ArrayList<Product>();
		try{
			products = productRepository.findProductByCategory(categoryId);
		}catch(DataAccessException e){
			logger.info("ERROR  ProductsController --> findProductByCategory: "+ e.getMessage());
			throw new ProductException("find product (where category) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ProductsController --> findProductByCategory");
		return products;
	}
	
	@RequestMapping("/findProductById/{productId}")
	public Product findProductById(@PathVariable(value="productId") Long productId) {
		
		logger.info("START ProductsController --> findProductById");
		Product product = new Product();
		try{
			product = productRepository.findProductById(productId);
		}catch(DataAccessException e){
			logger.info("ERROR  ProductsController --> findProductByCategory: "+ e.getMessage());
			throw new ProductException("find product (where category) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ProductsController --> findProductByCategory");
		return product;
	}
	
	@RequestMapping("/findProduct")
	public List<Product> findProduct() {

		logger.info("START ProductsController --> findProduct");
		List<Product> products = new ArrayList<Product>();
		try{
			products = productRepository.findProducts();
		}catch(DataAccessException e){
			logger.info("ERROR  ProductsController --> findProduct: "+ e.getMessage());
			throw new ProductException("find product error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ProductsController --> findProduct");
	    return products;
	}
	
	@RequestMapping("/findProductsRandom")
	public List<Product> findProductRandom() {
		
		logger.info("START ProductsController --> findProductRandom");
		List<Product> products = new ArrayList<Product>();
		try{
			products = productRepository.findProductsRandom();
		}catch(DataAccessException e){
			logger.info("ERROR  ProductsController --> findProductRandom: "+ e.getMessage());
			throw new ProductException("find random product error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END ProductsController --> findProductRandom");
	    return products;
	}	
}
