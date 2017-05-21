package it.univaq.ing.order.repository;

import org.springframework.data.repository.Repository;

import it.univaq.ing.order.domain.RelCartOrder;

/**
 * 
 * @author LC
 */
public interface RelCartOrderRepository extends Repository<RelCartOrder, Long>{
	
	public RelCartOrder save(RelCartOrder relCartOrder);
	
}
