package it.univaq.ing.accounts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.ing.accounts.domain.Address;

/**
 * 
 * @author LC
 */
public interface AddressRepository extends Repository<Address, Long>{

	@Query("FROM Address WHERE idAccount = :username")
	public List<Address> findAllByUser(String username);
	
	public Address save(Address address);
	
	@Query("FROM Address WHERE  idAddress = :idAddress")
	public Address findAddressById(Long idAddress);
	
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ADDRESS WHERE  ID_ACCOUNT IS NULL", nativeQuery = true)
    void deleteAddressNoAccount();
}
