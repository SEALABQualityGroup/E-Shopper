package it.univaq.ing.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.ing.cart.domain.CartItem;

/**
 * 
 * @author LC
 */
public interface CartRepository extends Repository<CartItem, Long>{

	public CartItem save(CartItem cart);
	
	@Query("FROM CartItem WHERE idCart = :idCart")
	public CartItem findCartItemById(@Param("idCart") Long idCart);
	
	@Query(value = "SELECT * FROM CART WHERE  ID_ACCOUNT =?1 AND FLAG_ORDER='N'", nativeQuery = true)
	public List<CartItem> findCartByUsername(String username);
	
	@Query(value = "SELECT * FROM CART WHERE  ID_ACCOUNT =?1 AND ID_ITEM =?2 AND FLAG_ORDER='N'", nativeQuery = true)
	public CartItem findCartByUsernameByIdItem(String username, Long idItem);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE CART SET FLAG_ORDER =?1 WHERE ID_ACCOUNT =?2", nativeQuery = true)
	public void updateListCartItem(String flagOrder, String username);
	
	@Query(value = "SELECT COUNT(ID_CART) FROM CART WHERE  ID_ACCOUNT =?1 AND FLAG_ORDER='N'", nativeQuery = true)
	public Integer countCartByUsername(String username);
	
    @Modifying
    @Transactional
    @Query("delete FROM CartItem c WHERE c.idCart = :idCart")
    void deleteCartItemById(@Param("idCart") Long idCart);
    
    @Modifying
    @Transactional
    @Query("delete FROM CartItem c WHERE c.idAccount = :idAccount")
    void deleteListCartItem(@Param("idAccount") String idAccount);
    
}
	