package it.univaq.ing.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.univaq.ing.order.domain.Order;

/**
 * 
 * @author LC
 */
public interface OrderRepository extends Repository<Order, Long>{

	@Query("FROM Order WHERE username = :username")
	public List<Order> findAllByUser(@Param("username")  String username);
	
	public Order save(Order order);
	
	@Query("FROM Order WHERE idAccount = :username and idStatus.idStatus in :idStatus")
	public List<Order> findAllByUserAndStatus(@Param("username") String username, @Param("idStatus") List<Integer> idStatus);
			
	@Query("FROM Order WHERE  idOrder = :idOrder")
	public Order findOrderById(@Param("idOrder") Long idOrder);
}
