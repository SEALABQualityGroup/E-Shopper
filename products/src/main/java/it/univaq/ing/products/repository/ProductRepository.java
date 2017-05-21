package it.univaq.ing.products.repository;

import java.util.List;

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

	@Query("FROM Product")
	public List<Product> findProducts();

	@Query("FROM Product WHERE category_id= :categoryId")
	public List<Product> findProductByCategory(@Param("categoryId") Long categoryId);
	
	@Query("FROM Product WHERE product_id= :productId")
	public Product findProductById(@Param("productId") Long productId);
	
	@Query(value = "SELECT * FROM PRODUCT  ORDER BY RAND() LIMIT 0,5", nativeQuery = true)
	public List<Product> findProductsRandom();
	
}
