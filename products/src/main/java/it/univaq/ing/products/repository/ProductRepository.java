package it.univaq.ing.products.repository;

import java.util.List;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.univaq.ing.products.domain.Product;
/**
 * 
 * @author LC
 *
 */
public interface ProductRepository extends Repository<Product, Long>{
	
	@NewSpan
	@Query("FROM Product")
	public List<Product> findProducts();
	
	@NewSpan
	@Query("FROM Product WHERE category_id= :categoryId")
	public List<Product> findProductByCategory(@Param("categoryId") Long categoryId);
	
	@NewSpan
	@Query("FROM Product WHERE product_id= :productId")
	public Product findProductById(@Param("productId") Long productId);
	
	@NewSpan
	@Query(value = "SELECT * FROM PRODUCT  ORDER BY RAND()", nativeQuery = true)
	public List<Product> findProductsRandom();
	
}
