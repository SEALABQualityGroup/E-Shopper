package it.univaq.ing.login.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import it.univaq.ing.login.domain.Login;

/**
 * Repository for Login data implemented using Spring Data JPA.
 * 
 * @author LC
 */
public interface LoginRepository extends Repository<Login, Long> {

	@Query("FROM Login WHERE idAccount = :username")
	public Login findByUsername(@Param("username") String username);
	
	public Login save(Login loginAccount);
}
