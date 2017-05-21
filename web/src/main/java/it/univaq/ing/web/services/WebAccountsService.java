package it.univaq.ing.web.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.security.model.Account;

/**
 * Hide the access to the microservice inside this local service.
 * 
 * @author LC
 */
@Service
public class WebAccountsService {

	@Autowired
	protected RestTemplate restTemplate;	

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebAccountsService.class.getName());

	public WebAccountsService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}	

	
	public Account signup(Account account){
		
		logger.info("START WebAccountsService --> signup --> username: "+account.getEmail());
		Account respAccount = null;
		try {
			respAccount =  restTemplate.postForObject(serviceUrl + "/signup", account, Account.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebAccountsService --> signup: "+e.getMessage());
			throw e;
		}finally{
			logger.info("END WebAccountsService --> signup --> username: "+account.getEmail());
		}
		return respAccount;
	}
	
	public Account updateAccount(Account account){
		
		logger.info("START WebAccountsService --> updateAccount --> username: "+account.getUsername());
		Account respAccount = null;
		try {
			respAccount =  restTemplate.postForObject(serviceUrl + "/updateAccount", account, Account.class);
		} catch (RestClientException e) {		
			logger.info("ERROR WebAccountsService --> updateAccount: "+e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> updateAccount --> username: "+account.getUsername());
		return respAccount;
	}
	
	public Boolean existingMail(String username){
		
		logger.info("START WebAccountsService --> existingMail --> mail: " +username);
		Boolean existingMail = Boolean.TRUE;
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);	
			existingMail = restTemplate.getForObject(serviceUrl + "/existingMail/{username}", Boolean.class, vars);
		} catch (RestClientException e) { 
			logger.info("ERROR WebAccountsService --> existingMail: " +e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> existingMail --> mail: " +username);
		return existingMail;
	}
	
	public Account findAccountByUsername(String username){
		
		logger.info("START WebAccountsService --> findAccountByUsername --> username: " +username);
		Account account = new Account();
		Map<String, String> vars = new HashMap<String, String>();
		try {
			vars.put("username", username);	
			account = restTemplate.getForObject(serviceUrl + "/findByUsername/{username}", Account.class, vars);
		} catch (RestClientException e) { 
			logger.info("ERROR WebAccountsService --> findAccountByUsername: " +e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> findAccountByUsername --> username: " +username);
		return account;
	}	
	
	public Account deleteAccount(Account account){
		logger.info("START WebAccountsService --> deleteAccount --> username: " +account.getUsername());
		try{	
			restTemplate.postForObject(serviceUrl + "/deleteAccount",account, Account.class);
		}catch(RestClientException e){
			logger.info("ERROR WebAccountsService --> deleteAccount: "+e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> deleteAccount --> username: " +account.getUsername());
		return account;
	}
	
	public void deleteAddressNoAccount(){
		logger.info("START WebAccountsService --> deleteAddressNoAccount");
		try{	
			restTemplate.delete(serviceUrl + "/deleteAddressNoAccount");
		}catch(RestClientException e){
			logger.info("ERROR WebAccountsService --> deleteAddressNoAccount: "+e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> deleteAddressNoAccount");
	}
	
	public void deletePaymentNoAccount(){
		logger.info("START WebAccountsService --> deletePaymentNoAccount");
		try{	
			restTemplate.delete(serviceUrl + "/deletePaymentNoAccount");
		}catch(RestClientException e){
			logger.info("ERROR  WebAccountsService --> deletePaymentNoAccount: "+e.getMessage());
			throw e;
		}
		logger.info("END WebAccountsService --> deletePaymentNoAccount");
	}
}