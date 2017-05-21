package it.univaq.ing.items.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.univaq.ing.items.domain.Item;


/**
 * 
 * @author LC
 */
public interface ItemRepository extends Repository<Item, Long>{
	
	
	@Query(value = "SELECT * FROM ITEM WHERE PRODUCT_ID = ?1", nativeQuery = true)
	public List<Item> getItemsByIdProduct(Long idProduct);	

	@Query(value = "SELECT * FROM ITEM WHERE CATEGORY_ID = ?1", nativeQuery = true)
	public List<Item> getItemsByIdCategory(Long idCategory);

	@Query("FROM Item WHERE itemId = :idItem")
	public Item getItemById(@Param("idItem") Long idItem);
	
	@Query(value = "SELECT * FROM ITEM WHERE CATEGORY_ID = ?1  AND UNIT_COST BETWEEN ?2 AND ?3", nativeQuery = true)
	public List<Item> findItemByCategoryFilter(Long idCategory, Long priceMin, Long priceMax);
	
	@Query(value = "SELECT * FROM ITEM WHERE PRODUCT_ID = ?1  AND UNIT_COST BETWEEN  ?2 AND ?3", nativeQuery = true)
	public List<Item> findItemByProductFilter(Long idProduct, Long priceMin, Long priceMax);
	
	@Query(value = "SELECT * FROM ITEM WHERE PRODUCT_ID = ?1 ORDER BY RAND() LIMIT 0,3", nativeQuery = true)
	public List<Item> findItemsRandomByIdProduct(Long idProduct);
	
	@Query(value = "SELECT * FROM ITEM  ORDER BY RAND() LIMIT 0,3", nativeQuery = true)
	public List<Item> findItemRandom();
	
	@Query(value = "SELECT * FROM ITEM WHERE TOT_QUANTITY < 4  ORDER BY RAND() LIMIT 0,3", nativeQuery = true)
	public List<Item> findFeaturesItemRandom();
	
	public Item save(Item account);
		
}
