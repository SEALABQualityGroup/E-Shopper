package it.univaq.ing.accounts.controller;

import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.netty.handler.codec.http.HttpResponseStatus;
import it.univaq.ing.accounts.AccountException;
import it.univaq.ing.accounts.domain.Account;
import it.univaq.ing.accounts.domain.Address;
import it.univaq.ing.accounts.repository.AccountRepository;
import it.univaq.ing.accounts.repository.AddressRepository;
import it.univaq.ing.accounts.repository.PaymentRepository;


/**
 * A RESTFul controller for accessing account information.
 * 
 * @author LC
 */
@RestController
public class AccountsController {

	protected Logger logger = Logger.getLogger(AccountsController.class
			.getName());
	protected AccountRepository accountRepository;
	protected AddressRepository addressRepository;
	protected PaymentRepository paymentRepository;

	/**
	 * Create an instance plugging in the respository of Accounts.
	 * 
	 * @param accountRepository
	 *            An account repository implementation.
	 */
	@Autowired
	public AccountsController(AccountRepository accountRepository, AddressRepository addressRepository, PaymentRepository paymentRepository) {
		this.accountRepository = accountRepository;
		this.addressRepository = addressRepository;
		this.paymentRepository = paymentRepository;

	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public Account signup(@RequestBody Account account){
		logger.info("START AccountsController --> signup --> USERNAME: "+ account.getUsername());
		Account accountResult  = new Account();
		
		try{
			//1 step --> Check if the account already exists --> if it is exists throw exception
			Account existingAccount = accountRepository.findUserByUsername(account.getUsername());
			if(existingAccount != null){
				throw new AccountException("existing account",HttpResponseStatus.BAD_REQUEST.code());
			}
			accountResult = this.saveAccount(account);
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController-->signup: "+ e.getMessage());
			throw new AccountException("save account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END AccountsController --> signup USERNAME: "+ account.getUsername());
		return accountResult;
	}
	
	@RequestMapping(value="/updateAccount", method = RequestMethod.POST)
	public Account updateAccount(@RequestBody Account account){
		logger.info("START AccountsController --> updateAccount --> USERNAME: "+ account.getUsername());
		Account accountResult  = new Account();	
		try{
			accountResult = this.saveAccount(account);
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController --> updateAccount -->  USERNAME:  "+ e.getMessage());
			throw new AccountException("updare account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END AccountsController --> updateAccount --> USERNAME:  "+ account.getUsername());
		return accountResult;
	}
	
	@RequestMapping(value="/existingMail/{username:.+}", method = RequestMethod.GET)
	public Boolean existingMail(@PathVariable(value="username") String username){
		logger.info("START AccountsController --> existingMail --> username: "+ username);
		Boolean existingMail  = Boolean.TRUE;
		try{
			//1 step --> Check if the account already exists --> if it is exists throw exception
			Account existingAccount = accountRepository.findUserByUsername(username);
			if(existingAccount == null){
				existingMail = Boolean.FALSE;
			}else{
				if(existingAccount.getFlagActive().equals("N")){
					throw new AccountException("account disable",HttpResponseStatus.BAD_REQUEST.code());
				}
			}
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController-->signup: "+ e.getMessage());
			throw new AccountException("save account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END AccountsController --> existingMail --> username: "+ username);
		return existingMail;
	}
	
	@RequestMapping(value="/findByUsername/{username:.+}", method = RequestMethod.GET)
	public Account findUserByUsername(@PathVariable(value="username") String username){
		logger.info("START AccountsController --> findUserByUsername --> username: "+ username);
		Account accountResult  = new Account();
		try{
			accountResult  = accountRepository.findUserByUsername(username);
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController--> findUserByUsername: "+ e.getMessage());
			throw new AccountException("find account (where username) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}	
		logger.info("END AccountsController --> findUserByUsername --> username: "+ username);
		return accountResult;
	}
	
	@RequestMapping(value="/deleteAccount", method = RequestMethod.POST)
	public void deleteAccount(@RequestBody Account account){
		logger.info("START AccountsController --> deleteAccount --> username: "+ account.getUsername());	
		try{
			accountRepository.deleteAccount(account.getUsername());
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController --> deleteAccount: "+ e.getMessage());
			throw new AccountException("delete account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END AccountsController --> deleteAccount --> username: "+ account.getUsername());	
	}
	
	@RequestMapping(value="/findAddressByIdAddress/{idAddress}", method = RequestMethod.GET)
	public Address findAddressByIdAddress(@PathVariable(value="idAddress") String idAddress){
		logger.info("STAR AccountsController --> findAddressByIdAddress: "+ idAddress);
		Address address = new Address();
		try{
			address = addressRepository.findAddressById(new Long(idAddress));
		}catch(DataAccessException e){
			logger.info("ERROR  AccountsController --> findAddressByIdAddress: "+ e.getMessage());
			throw new AccountException("find address id error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		}
		logger.info("END AccountsController --> findAddressByIdAddress: "+ idAddress);
		return address;
	}
	
	@RequestMapping(value="/signin", method=RequestMethod.POST)
	public Account login(@RequestBody Account  account){
		 logger.info("START AccountsController --> login: "+ account.getUsername());
		 Account accountResponse = new Account();
		 try{
			 accountResponse = accountRepository.signin(account.getEmail(), account.getPassword());
		 }catch(DataAccessException e){
			 logger.info("ERROR  AccountsController --> login: "+ e.getMessage());
				throw new AccountException("find account (login) error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		 }		
		 logger.info("END AccountsController --> login: "+ account.getUsername());
		 return accountResponse;
	}
	
	@RequestMapping(value="/deleteAddressNoAccount", method=RequestMethod.DELETE)
	public void deleteAddressNoAccount(){
		 logger.info("START AccountsController --> deleteAddressNoAccount");
		try{
			addressRepository.deleteAddressNoAccount();	
		}catch(DataAccessException e){
			 logger.info("ERROR  AccountsController --> deleteAddressNoAccount: "+ e.getMessage());
			 throw new AccountException("delete address no account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		 }
		 logger.info("END AccountsController --> deleteAddressNoAccount");
	}
	
	@RequestMapping(value="/deletePaymentNoAccount", method=RequestMethod.DELETE)
	public void deletePaymentNoAccount(){
		 logger.info("START AccountsController --> deletePaymentNoAccount");
		try{
			paymentRepository.deletePaymentNoAccount();
		}catch(DataAccessException e){
			 logger.info("ERROR  AccountsController --> deletePaymentNoAccount: "+ e.getMessage());
			 throw new AccountException("delete payment no account error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
		 }
		 logger.info("END AccountsController --> deletePaymentNoAccount");
	}
	
	private Account saveAccount(Account account){
		//set the insert  and modify date if a new account else only modify date
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    if(account.getInsertDate() == null){
			account.setInsertDate(sqldate);	
			account.setFlagActive("Y");
	    }
		account.setModifyDate(sqldate);
		return accountRepository.save(account);
	}
}
