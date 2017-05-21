package it.univaq.ing.accounts.repository;



import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.ing.accounts.domain.Account;


/**
 * Repository for Account data implemented using Spring Data JPA.
 * 
 * @author LC
 */
public interface AccountRepository extends Repository<Account, Long> {
	
	@Query("FROM Account WHERE name = :user and number = :pwd")
	public Account login(@Param("user") String user, @Param("pwd") String pwd);
	
	public Account save(Account account);
	
	@Query("FROM Account WHERE username = :username")
	public Account findUserByUsername(@Param("username") String username);
	
   @Modifying
   @Transactional
   @Query("delete FROM Account a WHERE a.username = :username")
   void deleteAccount(@Param("username") String username);
   
   
   	@Query(value = "SELECT acnt.* FROM LOGIN as l LEFT JOIN ACCOUNT  as acnt ON  l.ID_ACCOUNT = acnt.username WHERE  acnt.USERNAME= ?1  AND acnt.password =?2  AND  acnt.FLAG_ACTIVE = 'Y'", nativeQuery = true)
	public Account signin(@Param("user") String user, @Param("psw") String psw);

}