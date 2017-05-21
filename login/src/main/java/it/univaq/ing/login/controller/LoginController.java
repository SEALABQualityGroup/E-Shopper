package it.univaq.ing.login.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.login.LoginException;
import it.univaq.ing.login.domain.Login;
import it.univaq.ing.login.repository.LoginRepository;

/**
 * 
 * @author LC
 * 
 */
@RestController
public class LoginController {

	protected Logger logger = Logger.getLogger(LoginController.class
			.getName());
	protected LoginRepository loginRepository;

	/**
	 * Create an instance plugging in the repository of Login.
	 * 
	 * @param loginRepository
	 */
	@Autowired
	public LoginController(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	@RequestMapping(value="/findLoginByAccount", method = RequestMethod.POST)
	public Login findByUsername(@RequestBody Login login){
		logger.info("START LoginController --> findByUsername --> username: "+login.getIdAccount());		
		Login loginResult  = new Login();
		try{
			loginResult  = loginRepository.findByUsername(login.getIdAccount());
		}catch(DataAccessException e){
			logger.info("ERROR  LoginController --> findByUsername"+ e.getMessage());
			throw new LoginException("find login error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END LoginController --> findByUsername");		
		return loginResult;
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public Login save(@RequestBody Login login){
		logger.info("START LoginController --> save");	
		try{
			return loginRepository.save(login);
		}catch(DataAccessException e){
			logger.info("ERROR LoginController --> save"+ e.getMessage());
			throw new LoginException("save login error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
	}
}
