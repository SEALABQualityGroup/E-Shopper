package it.univaq.ing.categories.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.categories.CategoryException;
import it.univaq.ing.categories.domain.Category;
import it.univaq.ing.categories.repository.CategoryRepository;
import it.univaq.ing.categories.util.Experiment;
import it.univaq.ing.categories.util.SyntheticNoise;
/**
 * 
 * @author LC
 *
 */
@RestController
public class CategoriesController {
	
	protected Logger logger = Logger.getLogger(CategoriesController.class.getName());
	@Autowired Tracer tracer;
	
	@Value("#{'${experiment.getCategory}'.split(',')}")
	List<String> latencyInjections;

	@Value("#{'${noise.getCategory}'.split(',')}")
	List<String> syntheticNoise;


	protected CategoryRepository categoryRepository;

	@Autowired
	public CategoriesController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	@RequestMapping("/category")
	public List<Category> getCategory(){
		Span span = tracer.getCurrentSpan();
		Experiment.injectLatency(span, latencyInjections);
		SyntheticNoise.injectLatency(span, syntheticNoise);
		
		logger.info("START CategoriesController --> getCategory");
		List<Category> categories = new ArrayList<Category>();
		try{
			categories = categoryRepository.findAll();
		}catch(DataAccessException e){
			logger.info("ERROR  CategoriesController --> getCategory: "+ e.getMessage());
			throw new CategoryException("find category error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info(" END CategoriesController --> getCategory");
		return categories;
	}
	
}
