package it.univaq.ing.order.repository;

import org.springframework.data.repository.Repository;

import it.univaq.ing.order.domain.HistoryOrder;

/**
 * 
 * @author LC
 */
public interface HistoryOrderRepository extends Repository<HistoryOrder, Long>{
	
	public HistoryOrder save(HistoryOrder historyOrder);
	
}
