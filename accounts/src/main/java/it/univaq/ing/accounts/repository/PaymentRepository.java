package it.univaq.ing.accounts.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.ing.accounts.domain.Payment;

/**
 * 
 * @author LC
 */
public interface PaymentRepository extends Repository<Payment, Long>{

	@Query("FROM Payment WHERE  idPayment = :idPayment")
	public Payment findPaymentById(Long idPayment);
	
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PAYMENT WHERE  ID_ACCOUNT = null", nativeQuery = true)
    void deletePaymentNoAccount();
}
