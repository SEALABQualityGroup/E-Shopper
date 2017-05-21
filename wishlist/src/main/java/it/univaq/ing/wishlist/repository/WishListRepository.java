package it.univaq.ing.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.ing.wishlist.domain.WishList;

/**
 * 
 * @author LC
 */
public interface WishListRepository extends Repository<WishList, Long>{

	public WishList save(WishList wishList);
	
	@Query("FROM WishList WHERE idAccount = :username")
	public List<WishList> findWishListByUsername(@Param("username") String username);

	@Query(value = "SELECT * FROM WISH_LIST WHERE  ID_ACCOUNT =?1 AND ID_ITEM =?2", nativeQuery = true)
	public WishList findWishListByUsernameByIdItem(String username, Long idItem);
	
    @Modifying
    @Transactional
    @Query("delete FROM WishList w WHERE w.idWishlist = :idWishList")
    void deleteWishListById(@Param("idWishList") Long idWishList);
    
}
	