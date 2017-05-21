package it.univaq.ing.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.univaq.ing.order.domain.StatusOrder;

/**
 * 
 * @author LC
 */
public interface StatusOrderRepository extends Repository<StatusOrder, Long>{
	
	@Query("FROM StatusOrder WHERE  idStatus = :idStatus")
	public StatusOrder findStatusOrderById(@Param("idStatus") Integer idStatus);
	
	@Query("From StatusOrder")
	public List<StatusOrder> findAllStatusOrder();
}
