package it.univaq.ing.web.client;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.univaq.ing.web.model.Category;

//@FeignClient(name = "account-service")
public interface CategoriesServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/category", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	List<Category> findAll();

}
