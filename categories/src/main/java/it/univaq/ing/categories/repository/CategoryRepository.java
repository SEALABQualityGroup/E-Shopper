package it.univaq.ing.categories.repository;

import java.util.List;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.repository.Repository;

import it.univaq.ing.categories.domain.Category;

/**
 * 
 * @author LC
 */
public interface CategoryRepository extends Repository<Category, Long>{
	@NewSpan
	public List<Category> findAll();
	
}
