package it.univaq.ing.security.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.security.model.Account;

/**
 * 
 * @author LC
 */
@Service
public class WebSecurityService {
	
	public static final String ACCOUNTS_SERVICE_URL = "http://GATEWAY/accounts";
	
	@Autowired
	protected RestTemplate restTemplate;
	
	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebSecurityService.class.getName());

	public WebSecurityService() {
		this.serviceUrl = WebSecurityService.ACCOUNTS_SERVICE_URL;
	}

	public Account signin(Account account){
		logger.info("START WebSecurityService --> signin --> username: "+account.getUsername());
		Account accountResponse = null;
		try{
			accountResponse = restTemplate.postForObject(serviceUrl + "/signin", account, Account.class);
		} catch (RestClientException e) {
			logger.info("ERRORE WebSecurityService --> signin: "+e.getMessage());
			throw e;
		} 
		logger.info("END WebSecurityService --> signin --> username: "+account.getUsername());
		return accountResponse;
	}
}