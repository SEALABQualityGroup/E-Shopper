package it.univaq.ing.web.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import it.univaq.ing.web.model.Login;

/**
 * 
 * @author LC
 *
 */
public class WebLoginService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	protected Logger logger = Logger.getLogger(WebLoginService.class.getName());

	public WebLoginService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
	}
	
	public Login findLoginByAccount(Login login){
		logger.info("START WebLoginService --> findLoginByAccount");
		try{
			login =  restTemplate.postForObject(serviceUrl + "/findLoginByAccount",login, Login.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebLoginService --> findLoginByAccount: "+e.getMessage());
			throw e;
		}
		logger.info("END WebLoginService --> findLoginByAccount");
		return login;
	}

	public Login save(Login login) {
		logger.info("START WebLoginService --> save");
		Login respLogin = null;
		try{
			respLogin = restTemplate.postForObject(serviceUrl + "/save", login, Login.class);
		}catch (RestClientException e) {		
			logger.info("ERROR WebLoginService --> save: "+e.getMessage());
			throw e;
		}
		logger.info("END WebLoginService --> save");
		return respLogin;
	}
}
