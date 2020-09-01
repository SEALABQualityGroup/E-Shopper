package it.univaq.ing.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univaq.ing.security.UserPrincipal;
import it.univaq.ing.security.model.Account;
import it.univaq.ing.security.model.Address;
import it.univaq.ing.security.model.GiftCard;
import it.univaq.ing.security.model.Payment;
import it.univaq.ing.security.services.WebSecurityService;
import it.univaq.ing.web.model.Category;
import it.univaq.ing.web.model.ErrorModel;
import it.univaq.ing.web.model.Item;
import it.univaq.ing.web.model.Login;
import it.univaq.ing.web.model.Product;
import it.univaq.ing.web.services.WebAccountsService;
import it.univaq.ing.web.services.WebCartService;
import it.univaq.ing.web.services.WebCategoriesService;
import it.univaq.ing.web.services.WebItemsService;
import it.univaq.ing.web.services.WebLoginService;
import it.univaq.ing.web.services.WebProductsService;

/**
 * Client controller, fetches Account info from the microservice via
 * {@link WebAccountsService}.
 * 
 * @author LC
 */
@Controller
@SessionAttributes(value = { "accountName", "countCartItem" })
public class WebAccountsController {

	@Autowired
	protected WebAccountsService accountsService;

	@Autowired
	protected WebSecurityService securityService;

	@Autowired
	protected WebLoginService loginService;

	@Autowired
	protected WebCategoriesService categoriesService;

	@Autowired
	protected WebProductsService productsService;

	@Autowired
	protected WebItemsService itemsService;

	@Autowired
	protected WebCartService cartService;

	@Inject
	private ObjectMapper objectMapper;

	protected Logger logger = Logger.getLogger(WebAccountsController.class.getName());

	public WebAccountsController(WebAccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String doLogout(Account account, Model model) throws ServletException {
		logger.info("START WebAccountsController --> doLogin");

		model.addAttribute("accountName", null);
		model.addAttribute("account", new Account());
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(Account account, Model model) throws ServletException {

		logger.info("START WebAccountsController --> doLogin");
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.isAuthenticated()
					&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				Integer countCartItem = cartService.countCartItemByUsername(accountSession.getUsername());
				model.addAttribute("countCartItem", countCartItem);
				model.addAttribute("account", accountSession);
				model.addAttribute("accountName", accountSession.getName());
				model.addAttribute("path", "/login");
				logger.info("END WebAccountsController --> doLogin");
				return "redirect:/";
			} else {
				logger.info("END WebAccountsController --> doLogin");
				return "redirect:/login";
			}
		} catch (RestClientException e) {
			logger.info("ERROR WebAccountsController --> doLogin: " + e.getMessage());
			return "login";
		}
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Model model)
			throws ServletException, MalformedURLException, InterruptedException, ExecutionException {

		logger.info("START WebAccountsController --> contact");
		try{
			List<Category> categories = categoriesService.findAll();
			List<Product> products = productsService.findProductsRandom();
			List<Item> items = itemsService.findItemsRandomByIdProduct(products.get(0).getProductId());
			CompletableFuture<List<Item>> itemsRecommended = itemsService.findItemsRandom();
			
			model.addAttribute("categories", categories);
			model.addAttribute("products", products);
			model.addAttribute("items", items);
			model.addAttribute("itemsRecommended", itemsRecommended.get());
			model.addAttribute("path", "/contact");

		}catch (RestClientException e) {
			logger.info("ERROR WebAccountsController --> contact: "+e.getMessage());
			return "index";
		}
		logger.info("END WebAccountsController --> contact");
		return "contacUS";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET )
	public String getLoginPage(Model model, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) throws ServletException {
		logger.info("START WebAccountsController --> getLoginPage");
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.isAuthenticated() && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				Account accountSession = (Account) auth.getDetails();
				Integer countCartItem = cartService.countCartItemByUsername(accountSession.getUsername());
				model.addAttribute("countCartItem", countCartItem);
				model.addAttribute("account", accountSession);
				model.addAttribute("accountName", accountSession.getName());
				logger.info("END WebAccountsController --> getLoginPage");
				return "redirect:/";
			}else{
				if(error != null){
					model.addAttribute("noAccount", Boolean.TRUE);
				}
				if(logout != null){
					model.addAttribute("accountName", "");
					return "redirect:/";
				}
				Account account = new Account();
				String usernameSession = (String) model.asMap().get("accountName");
				if(usernameSession != "" && usernameSession != null){
					model.addAttribute("updatePassword", Boolean.TRUE);
					model.addAttribute("accountName", "");				
				}
				model.addAttribute("account", account);
				model.addAttribute("path", "/login");
				logger.info("END WebAccountsController --> getLoginPage");
				return "login";
			}
		}catch (RestClientException e) {
			logger.info("ERROR WebAccountsController --> getLoginPage: "+e.getMessage());
			return "index";
		}
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(Model model, @ModelAttribute("account") Account account, @RequestParam("pwdRepeat") String pwdRepeat) throws InterruptedException, ExecutionException {
		logger.info("START WebAccountsController --> signup");
		try{
			String username = account.getEmail();
			account.setUsername(username);
			account.setPassword(this.cryptoPassword(account.getPassword()));
			Account accountResponse = accountsService.signup(account);	
			if(accountResponse == null){
				model.addAttribute("errorSaveAccount", Boolean.TRUE );
				return "login";
			}			
			Login login = new Login();
			login.setIdAccount(account.getUsername());
			login.setPassword(accountResponse.getPassword());
			Calendar currenttime = Calendar.getInstance();
		    Date sqldate = new Date((currenttime.getTime()).getTime());
		    login.setInsertDate(sqldate);
		    login.setModifyDate(sqldate);
			Login loginResponse = loginService.save(login);
			if(loginResponse ==  null){
				model.addAttribute("errorSaveAccount", Boolean.TRUE );
				return "login";
			}		
			this.getHomeProduct(model);
			model.addAttribute("accountSuccess", Boolean.TRUE);	
		}catch(RestClientException e){		
			try {
				ErrorModel err = objectMapper.readValue(((org.springframework.web.client.HttpServerErrorException)e).getResponseBodyAsByteArray(), ErrorModel.class);
				if(err.getMessage().equals("existing account")){
					model.addAttribute("existenceEmail", Boolean.TRUE );	
					return "login";
				}		
				model.addAttribute("errorSaveAccount", Boolean.TRUE );
				logger.info("ERROR WebAccountsController -->signup: " +e.getMessage());
				return "login";	
			} catch (IOException e1) {
				logger.info("ERROR WebAccountsController -->signup in ErrorModel " +e.getMessage());
			}
		}
		logger.info("END WebAccountsController --signup : "+account.getEmail());
		return "index";
	}
	
	@RequestMapping(value="/forgotPasswordLogin", method=RequestMethod.GET)
	public String forgotPasswordLogin(Model model, HttpServletRequest request){
		
		logger.info("START WebAccountsController --> forgotPasswordLogin");
		this.getUrlBack(model, request);
		model.addAttribute("path", "/login");
		return "forgotPasswordLogin";	
	}
	
	@RequestMapping(value="/sendEmailForgotPassword", method=RequestMethod.POST)
	public String sendEmailForgotPassword(Model model,@RequestParam("emailControl") String emailControl){
		
		logger.info("START WebAccountsController -->sendEmailForgotPassword : "+emailControl);
		try{
			Boolean existingMail = accountsService.existingMail(emailControl);
			model.addAttribute("path", "/login");
			if(!existingMail){
				model.addAttribute("invalideEmail", Boolean.TRUE);
				logger.info("END WebAccountsController --> sendEmailForgotPassword : "+emailControl);
				return "forgotPasswordLogin";		
			}else{
				logger.info("END WebAccountsController --> sendEmailForgotPassword : "+emailControl);
				return "successSendMail";
			}	
		}catch (RestClientException e) {
			try {
				model.addAttribute("path", "/login");
				ErrorModel err = objectMapper.readValue(((org.springframework.web.client.HttpServerErrorException)e).getResponseBodyAsByteArray(), ErrorModel.class);
				if(err.getMessage().equals("account disable")){
					model.addAttribute("invalideUser", Boolean.TRUE );	
					return "forgotPasswordLogin";
				}		
				logger.info("ERROR WebAccountsController --> sendEmailForgotPassword: " +e.getMessage());
				model.addAttribute("invalideEmail", Boolean.TRUE);
				return "forgotPasswordLogin";	
			} catch (IOException e1) {
				logger.info("ERROR WebAccountsController --> sendEmailForgotPassword in ErrorModel " +e.getMessage());
			}
			return "forgotPasswordLogin";
		}
	}
	
	@RequestMapping("/closeMail")
	public String closeMail(){
		
		logger.info("START WebAccountsController --> closeMail");
		return "redirect:/";		
	}
	
	
	//  ----------------------------------------------------------------
	//  -------------------------ACCOUNT SETTINGS-----------------------
	//  ----------------------------------------------------------------
	
	//  ----------------------------------------------------------------
	//  -------------------------FORGOT PASSWORD ACCOUNT SETTINGS-------
	//  ----------------------------------------------------------------
	@RequestMapping(value="/forgotPassword", method=RequestMethod.GET)
	public String forgotPassword(Model model,  HttpServletRequest request){	
		
		logger.info("START WebAccountsController --> forgotPassword");
		this.getUrlBack(model, request);
		model.addAttribute("path", "/account");
		return "forgotPassword";	
	}
	
	@RequestMapping(value="/step1ForgotPassword", method=RequestMethod.POST)
	public String step1ForgotPassword(Model model,  HttpServletRequest request, @RequestParam("emailControl") String emailControl){
		
		logger.info("START WebAccountsController --> step1ForgotPassword");
		model.addAttribute("path", "/account");
		try{
			this.getUrlBack(model, request);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			if(accountSession.getEmail().equals(emailControl)){
				logger.info("END WebAccountsController --> step1ForgotPassword");
				return "step1ForgotPassword";
			}else{
				model.addAttribute("invalideEmail", Boolean.TRUE);
				logger.info("END WebAccountsController --> step1ForgotPassword");
				return "forgotPassword";
			}			
		}catch (RestClientException e) {
			logger.info("ERROR WebAccountsController --> step1ForgotPassword: "+e.getMessage());
			model.addAttribute("invalideEmail", Boolean.TRUE);
			return "forgotPassword";	
		}
	}
	
	@RequestMapping(value="/saveNewPassword", method=RequestMethod.POST)
	public String saveNewPassword(Model model,  @RequestParam("pwd") String pwd,  @RequestParam("pwdRepeat") String pwdRepeat){
		
		logger.info("START WebAccountsController --> saveNewPassword");
		model.addAttribute("path", "/account");
		try{
			if(!pwd.equals(pwdRepeat)){
				model.addAttribute("errorePswMatch", Boolean.TRUE);
				logger.info("END WebAccountsController --> saveNewPassword");
				return "step1ForgotPassword";	
			}else{
				this.savePassword(pwd, model);
				logger.info("END WebAccountsController --> saveNewPassword");
				SecurityContextHolder.clearContext();
				return "redirect:/login";
			}			
		}catch (RestClientException e) {
			logger.info("ERROR WebAccountsController --> saveNewPassword: "+e.getMessage());
			model.addAttribute("errorSave", Boolean.TRUE);
			return "step1ForgotPassword";	
		}	
	}
	
	@RequestMapping(value="/sendMail")
	public String sendMail(Model model) {
		
		logger.info("START WebAccountsController --> sendMail");
		
		model.addAttribute("path", "/account");
		model.addAttribute("sendMail", Boolean.TRUE);	
		return "contacUS";
	}
	
	@RequestMapping(value="/backAddress")
	public String backAddress(Model model, HttpServletRequest request) {
		
		logger.info("START WebAccountsController --> backAddress");
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;
	}
	
	//  ------------------------------------------------------------------------------------------------------------
	//  -------------------------LOGIN AND SECURITY SETTINGS--------------------------------------------------------
	//  ------------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/loginSetting", method=RequestMethod.GET)
	public String loginSetting(Model model){
		
		logger.info("START WebAccountsController --> loginSetting");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		model.addAttribute("account", accountSession);
		model.addAttribute("path", "/account");
		return "loginSetting";	
	}
	
	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=nameChange")
	public String nameChange(Model model, HttpServletRequest request, @ModelAttribute("account") Account account) {
		
		logger.info("START WebAccountsController --> nameChange");
		this.getUrlBack(model, request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		model.addAttribute("account", accountSession);
		model.addAttribute("path", "/account");
		return "nameChange";
	}
	
	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=emailChange")
	public String emailChange(Model model, HttpServletRequest request, @ModelAttribute("account") Account account) {
		
		logger.info("START WebAccountsController --> emailChange");
		this.getUrlBack(model, request);
		model.addAttribute("path", "/account");
		return "emailChange";
	}

	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=pswChange")
	public String pswChange(Model model, HttpServletRequest request, @ModelAttribute("account") Account account) {
		
		logger.info("START WebAccountsController --> pswChange");
		this.getUrlBack(model, request);
	
		model.addAttribute("path", "/account");
		return "pswChange";
	}
	
	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=saveEmailChange")
	public String saveEmailChange(Model model, @RequestParam("newEmail") String newEmail) {
		
		logger.info("START WebAccountsController --> saveEmailChange");
		Calendar currenttime = Calendar.getInstance();
		Date sqldate = new Date((currenttime.getTime()).getTime());
		model.addAttribute("path", "/account");
		try{
			//1. CHECK THAT THE NEW MAIL IS CORRECTLY ARRIVED
			if(newEmail != null){
				//2. CHECKING THE MAIL IS NOT EXPLAINED
				Account accountResponse = accountsService.findAccountByUsername(newEmail);		
				if(accountResponse != null){
					model.addAttribute("emailExisting", Boolean.TRUE);
					return "emailChange";
				}else{
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					Account accountSession = (Account) auth.getDetails();
					
					Account accountDelete  = new Account();
					accountDelete.setBalance(accountSession.getBalance());
					accountDelete.setEmail(accountSession.getEmail());
					accountDelete.setFlagActive(accountSession.getFlagActive());
					accountDelete.setInsertDate(accountSession.getInsertDate());
					accountDelete.setModifyDate(accountSession.getModifyDate());
					accountDelete.setName(accountSession.getName());
					accountDelete.setPassword(accountSession.getPassword());
					accountDelete.setUsername(accountSession.getUsername());
			    
					accountSession.setEmail(newEmail);
					accountSession.setUsername(newEmail);					
					Account accountResult = accountsService.updateAccount(accountSession);
					model.addAttribute("account", accountResult);	
					
					Login login = new Login();
					login.setIdAccount(accountSession.getUsername());
					Login loginFind = loginService.findLoginByAccount(login);
					loginFind.setIdAccount(accountResult.getUsername());
					loginFind.setModifyDate(sqldate);
					loginService.save(loginFind);
					accountsService.deleteAccount(accountDelete);		    	
				}		
			}	
		}catch(RestClientException e){
			logger.info("ERROR WebAccountController --> saveEmailChange: "+e);
			model.addAttribute("erroreSaveEmail", Boolean.TRUE);
			return "emailChange";
		}
		logger.info("END WebAccountsController --> saveEmailChange");
		return "loginSetting";		
	}
	
	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=saveNameChange")
	public String saveNameChange(Model model, @RequestParam("newName") String newName) {
		
		logger.info("START WebAccountsController --> saveNameChange");
		model.addAttribute("path", "/account");
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			if(newName != null){
				accountSession.setName(newName);	
				Account accountResponse = accountsService.updateAccount(accountSession);
				accountSession.setName(accountResponse.getName());
				model.addAttribute("account", accountResponse);
				model.addAttribute("accountName", accountResponse.getName());
			}		
		}catch(RestClientException e){
			logger.info("ERROR WebAccountController --> saveNameChange: "+e);
			model.addAttribute("erroreSaveName", Boolean.TRUE);
			return "nameChange";
		}
		logger.info("END WebAccountsController --> saveNameChange");
		return "loginSetting";
		
	}
	
	@RequestMapping(value="/changeAccountSetting", method=RequestMethod.POST, params="action=savePswChange")
	public String savePswChange(Model model, @RequestParam("psw") String psw, @RequestParam("pwdRepeat") String pwdRepeat, @RequestParam("oldPsw") String oldPsw) {
		
		logger.info("START WebAccountsController --> savePswChange");
		model.addAttribute("path", "/account");
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			Account accountSession = (Account) auth.getDetails();

			//1. Check if the password you entered is correct
			if(!accountSession.getPassword().equals(this.cryptoPassword(oldPsw))){
				model.addAttribute("errorePsw", Boolean.TRUE);
				logger.info("END WebAccountsController --> savePswChange");
				return "pswChange";	
			}
			//2. Check if the new password and the password repeat match
			if(!psw.equals(pwdRepeat)){
				model.addAttribute("errorePswMatch", Boolean.TRUE);
				logger.info("END WebAccountsController --> savePswChange");
				return "pswChange";	
			}
			//3. If all controls go ok
			this.savePassword(psw, model);	   
		}catch(RestClientException e){
			logger.info("ERROR WebAccountController --> savePswChange: "+e);
			model.addAttribute("erroreSavePsw", Boolean.TRUE);
			return "pswChange";
		}
			logger.info("END WebAccountsController --> savePswChange");
			SecurityContextHolder.clearContext();
			return "redirect:/login";	
	}

	//  ------------------------------------------------------------------------------------------------------------
	//  -------------------------DISABLE ACCOUNT SETTINGS----------------------------------------------------------
	//  ------------------------------------------------------------------------------------------------------------
	@RequestMapping(value="/disableAccount", method=RequestMethod.GET)
	public String disableAccount(Model model){
		
		logger.info("START WebAccountsController --> disableAccount");
		
		model.addAttribute("path", "/account");
		return "disableAccount";	
	}
	
	@RequestMapping(value="/saveDisableAccount", method=RequestMethod.POST, params="action=disable")
	public String saveDisableAccount(Model model){
		
		logger.info("START WebAccountsController --> saveDisableAccount");
		model.addAttribute("path", "/account");
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			accountSession.setFlagActive("N");
			Account accountResponse= accountsService.updateAccount(accountSession);
			if(accountResponse != null)
			model.addAttribute("accountDisable", Boolean.TRUE);
			logger.info("END WebAccountsController --> saveDisableAccount");
			return "redirect:/logout";
		}catch(RestClientException e){
			logger.info("ERROR WebAccountController --> saveDisableAccount: "+e);
			model.addAttribute("erroreSavePsw", Boolean.TRUE);
			return "disableAccount";
		}
	}
	
	@RequestMapping(value="/saveDisableAccount", method=RequestMethod.POST, params="action=noDisable")
	public String noDisableAccount(Model model){
		
		logger.info("START WebAccountsController --> disableAccount");
		model.addAttribute("path", "/account");
		return "manageAccount";	
	}
	
	//  ----------------------------------------------------------------
	//  -----------------MANAGE ACCOUNT---------------------------------
	//  ----------------------------------------------------------------
	
	@RequestMapping(value="/manageAccount", method=RequestMethod.GET)
	public String manageAccount(Model model){		
		
		logger.info("START WebAccountsController --> manageAccount");
		model.addAttribute("path", "/account");
		return "manageAccount";	
	}
	
	@RequestMapping(value="/accountSetting", method=RequestMethod.GET)
	public String accountSetting(Model model){
		
		logger.info("START WebAccountsController --> accountSetting");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		model.addAttribute("account", accountSession);
		model.addAttribute("path", "/account");
		return "loginSetting";
	}
	
	@RequestMapping(value="/addressBook", method=RequestMethod.GET)
	public String addressBook(Model model){
		
		logger.info("START WebAccountsController --> addressBook");
		model.addAttribute("path", "/account");
		return "addressBook";	
	}
	
	@RequestMapping(value="/paymentMethods", method=RequestMethod.GET)
	public String paymentMethods(Model model){
		
		logger.info("START WebAccountsController --> paymentMethods");
		model.addAttribute("path", "/account");
		return "paymentMethods";	
	}
	
	//  ----------------------------------------------------------------
	//  -------------------------SETTINGS---------------------------------
	//  ----------------------------------------------------------------
	
	@RequestMapping(value="/manageAddressBook", method=RequestMethod.GET)
	public String manageAddressBook(Model model, HttpServletRequest  request){
		
		logger.info("START WebAccountsController --> manageAddressBook");
		Boolean noAddressList  = Boolean.TRUE;
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			if(accountSession.getListAddress().size() == 0){
				noAddressList = Boolean.TRUE;
			}else{
				for(Address address : accountSession.getListAddress()){
					if(address.getFlagActive().equals("Y")){
						noAddressList = Boolean.FALSE;
						break;
					}
				}
			}
			model.addAttribute("noAddressList",noAddressList);
			model.addAttribute("account",accountSession);
			model.addAttribute("path", "/account");
		}catch(RestClientException e){
			logger.info("ERROR WebAccountController --> manageAddressBook: "+e);
			model.addAttribute("noAddressList",noAddressList);
			model.addAttribute("path", "/account");
			return "manageAddressBook";
		}
		logger.info("END WebAccountsController --> manageAddressBook");
		return "manageAddressBook";	
	}
	
	@RequestMapping(value="/addAddressBook/{provenienza}", method=RequestMethod.GET)
	public String addAddressBook(Model model, HttpServletRequest request, @PathVariable String  provenienza){
		
		logger.info("START WebAccountsController --> addAddressBook");
		Address address = new Address();
		this.getUrlBack(model, request);
		model.addAttribute("address", address);
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		return "addAddressBook";	
	}
	
	
	@RequestMapping(value="/saveNewAddress/{provenienza}", method=RequestMethod.POST, params="action=saveEContinue")
	public String saveEContinue(Model model, @ModelAttribute("address") Address address, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> saveEContinue");
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			this.saveAddress(model, address);
			model.addAttribute("addAddress", Boolean.TRUE);
			if(provenienza.equals("cart")){
				logger.info("END WebAccountsController --> saveEContinue");
				return "redirect:/checkout";
			}
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> saveEContinue: "+e.getMessage());
			model.addAttribute("errorSave", Boolean.TRUE);
			model.addAttribute("provenienza", provenienza);
			return "addAddressBook";		
		}
		logger.info("END WebAccountsController --> saveEContinue");
		return "manageAddressBook";
	}
	
	@RequestMapping(value="/saveNewAddress/{provenienza}", method=RequestMethod.POST, params="action=saveEPayment")
	public String saveEPayment(Model model, HttpServletRequest request, @ModelAttribute("address") Address address, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> saveEPayment");
		Payment payment = new Payment();
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			this.saveAddress(model, address);		
			this.settingPayment(model);
			String referer = request.getHeader("Referer");
			String[] value = referer.split("/");
			String urlBack = value[3]+'/'+value[4];
			
			if(value[3].equals("saveNewAddress")){
				urlBack ="manageAddressBook";
			}
			model.addAttribute("urlBack", urlBack);
			model.addAttribute("payment", payment);
			model.addAttribute("addAddress", Boolean.TRUE);
			model.addAttribute("provenienza", provenienza);
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> saveEPayment: "+e.getMessage());
			model.addAttribute("erroreSave", Boolean.TRUE);
			model.addAttribute("provenienza", provenienza);
			return "addAddressBook";			
		}
		logger.info("END WebAccountsController --> saveEPayment");
		return "addCreditCard";
	}
	

	@RequestMapping(value="/editAddressSetting/{idAddress}/{provenienza}")
	public String editAddress(Model model, HttpServletRequest request, @PathVariable Long  idAddress, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> editAddress");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		this.getUrlBack(model, request);
		for (Address address :accountSession.getListAddress()){
			if(idAddress.equals(address.getIdAddress())){
				model.addAttribute("address", address );	
			}
		}
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		model.addAttribute("provenienza", provenienza);
		logger.info("END WebAccountsController --> editAddress");
		return "editAddress";
	}
	
	
	@RequestMapping(value="/saveEditAddress/{idAddress}/{provenienza}", method=RequestMethod.POST)
	public String saveEditFromManage(Model model, @ModelAttribute("address") Address address,  @PathVariable Long idAddress, @PathVariable String provenienza) {
		
		logger.info("START WebAccountsController --> saveEditFromManage");
	
		try{
			address.setIdAddress(idAddress);
			this.saveAddress(model, address);
			model.addAttribute("editAddress", Boolean.TRUE);
			model.addAttribute("noAddressList",Boolean.FALSE);
			if(provenienza.equals("cart")){
				logger.info("END WebAccountsController --> saveEditFromManage");
				return "redirect:/checkout";
			}
		}catch (RestClientException e){
			model.addAttribute("erroreSave", Boolean.TRUE);
			model.addAttribute("address", address);
			model.addAttribute("provenienza", provenienza);
			model.addAttribute("path", "/account");
			logger.info("ERROR WebAccountsController --> saveEditFromManage: "+e.getMessage());
			return "editAddress";			
		}
		logger.info("END WebAccountsController --> saveEditFromManage");
		model.addAttribute("path", "/account");
		return "manageAddressBook";
	}

	
	@RequestMapping(value="/addAddressSetting/{provenienza}")
	public String newAddress(Model model, HttpServletRequest request, @PathVariable String provenienza){
		
		logger.info("START WebAccountsController --> newAddress");
		Address address = new Address();
		this.getUrlBack(model, request);
		model.addAttribute("address", address);
		model.addAttribute("provenienza", provenienza);
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		return "addAddressBook";	
	}
	
	@RequestMapping(value="/deleteAddressSetting/{idAddress}/{provenienza}")
	public String deleteAddressSetting(Model model, @PathVariable Long  idAddress, @PathVariable String provenienza){
		
		logger.info("START WebAccountsController --> deleteAddressSetting");
		Boolean noAddressList = Boolean.TRUE;
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{	
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			for(Address address: accountSession.getListAddress()){
				if(address.getIdAddress().equals(idAddress)){
					address.setFlagActive("N");
					break;
				}
			}
			Account accountResponse = accountsService.updateAccount(accountSession);
			accountSession.setListAddress(accountResponse.getListAddress());
			model.addAttribute("account", accountResponse);
			
			if(accountResponse.getListAddress().size() == 0){
				noAddressList = Boolean.TRUE;
			}else{
				for(Address address : accountResponse.getListAddress()){
					if(address.getFlagActive().equals("Y")){
						noAddressList = Boolean.FALSE;
						break;
					}
				}
			}
			model.addAttribute("noAddressList", noAddressList);
			model.addAttribute("provenienza", provenienza);
			model.addAttribute("deleteAddress", Boolean.TRUE);	
			if(provenienza.equals("cart")){
				return "redirect:/checkout";
			}
		}catch (RestClientException e){
			model.addAttribute("erroreSave", Boolean.TRUE);
			logger.info("ERROR WebAccountsController --> deleteAddressSetting: "+e.getMessage());	
			if(provenienza.equals("cart")){
				return "redirect:/checkout";
			}
			model.addAttribute("noAddressList", Boolean.FALSE);
			return "manageAddressBook";	
		}
		logger.info("END WebAccountsController --> deleteAddressSetting");	
		return "manageAddressBook";	
	}
	
	//  ----------------------------------------------------------------
	//  -------------------------PAYMENT METHOD---------------------------------
	//  ----------------------------------------------------------------
	
	@RequestMapping(value="/managePaymentOption", method=RequestMethod.GET)
	public String managePaymentOption(Model model, HttpServletRequest request){
		
		logger.info("START WebAccountsController --> managePaymentOption");	
		Boolean noPaymentList  = Boolean.TRUE;
		this.getUrlBack(model, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		if(accountSession.getListPayment().size() == 0){
			noPaymentList = Boolean.TRUE;
		}else{
			for(Payment payment : accountSession.getListPayment()){
				payment.getCreditCardExpire();
				if(payment.getFlagActive().equals("Y")){
					noPaymentList = Boolean.FALSE;
					break;
				}
			}
		}
		model.addAttribute("account",accountSession);
		model.addAttribute("noPaymentList",noPaymentList);
		model.addAttribute("path", "/account");
		logger.info("END WebAccountsController --> managePaymentOption");
		return "managePaymentOption";
	}
	
	@RequestMapping(value="/addCreditCard/{provenienza}")
	public String addCreditCard(Model model, HttpServletRequest request, @PathVariable String  provenienza){
		
		logger.info("START WebAccountsController --> addCreditCard"); 
		this.getUrlBack(model, request);

		this.settingPayment(model);
        model.addAttribute("provenienza", provenienza); 
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		return "addCreditCard";	
	}	
	
	@RequestMapping(value="/saveNewPayment/{provenienza}", method=RequestMethod.POST)
	public String saveNewPayment(Model model, @ModelAttribute("payment") Payment payment, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> saveNewPayment");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);	
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			model.addAttribute("provenienza", provenienza);
			//Add Valid Checks if the month you entered is <per month in progress.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			for(Payment paymentSession : accountSession.getListPayment()){
				if(paymentSession.getNumber().equals(payment.getNumber())){
					model.addAttribute("creditCardError", Boolean.TRUE);
					this.settingPayment(model);
					model.addAttribute("payment", payment);
					logger.info("END WebAccountsController --> saveNewPayment");
					return "addCreditCard";	
				}
			}
			if(payment.getExpirationYear() < year){
				this.settingPayment(model);
				model.addAttribute("errorDate", Boolean.TRUE);
				model.addAttribute("payment", payment);
				logger.info("END WebAccountsController --> saveNewPayment");
				return "addCreditCard";
			}else{
				if( payment.getExpirationYear() == year && new Integer(payment.getExpirationMonth()) < month){				
					this.settingPayment(model);
					model.addAttribute("errorDate", Boolean.TRUE);
					model.addAttribute("payment", payment);
					logger.info("END WebAccountsController --> saveNewPayment");
					return "addCreditCard";
				}
			}		
			this.savePayment(model, payment);
			model.addAttribute("addPayment", Boolean.TRUE);
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> saveNewPayment: "+e.getMessage());
			model.addAttribute("erroreSave", Boolean.TRUE);
			this.settingPayment(model);
			return "addCreditCard";		
		}
		logger.info("END WebAccountsController --> saveNewPayment");
		model.addAttribute("noPaymentList", Boolean.FALSE);		
		if(provenienza.equals("cart")){
			return "redirect:/checkout";
		}	
		if(provenienza.equals("address")){
			model.addAttribute("noAddressList", Boolean.FALSE);
			return "manageAddressBook";
		}	
		return "managePaymentOption";
	}
	
	@RequestMapping(value="/deleteCreditCardSetting/{idPayment}/{provenienza}")
	public String deleteCreditCardSetting(Model model, @PathVariable Long  idPayment, @PathVariable String  provenienza){
		
		logger.info("START WebAccountsController --> deleteCreditCardSetting");
		Boolean noPaymentList = Boolean.TRUE;
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			model.addAttribute("provenienza", provenienza);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			for(Payment payment: accountSession.getListPayment()){
				if(payment.getIdPayment().equals(idPayment)){
					payment.setFlagActive("N");
					break;
				}
			}
			Account accountResponse = accountsService.updateAccount(accountSession);
			accountSession.setListPayment(accountResponse.getListPayment());
			model.addAttribute("account", accountResponse);
			
			if(accountResponse.getListPayment().size() == 0){
				noPaymentList = Boolean.TRUE;
			}else{
				for(Payment payment : accountResponse.getListPayment()){
					if(payment.getFlagActive().equals("Y")){
						noPaymentList = Boolean.FALSE;
						break;
					}
				}
			}
			model.addAttribute("noPaymentList", noPaymentList);		
			accountsService.deletePaymentNoAccount();	
			model.addAttribute("deletePayment", Boolean.TRUE);	
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> deleteCreditCardSetting: "+e.getMessage());
			model.addAttribute("errorSave", Boolean.TRUE);
			model.addAttribute("noAddressList", Boolean.FALSE);
			if(provenienza.equals("cart")){
				return "redirect:/checkout";
			}	
			return "managePaymentOption";	
		}
		logger.info("END WebAccountsController --> deleteCreditCardSetting");
		if(provenienza.equals("cart")){
			return "redirect:/checkout";
		}	
		return "managePaymentOption";	
	}
	
	@RequestMapping(value="/editCreditCardSetting/{idPayment}/{provenienza}")
	public String editCreditCardSetting(Model model, HttpServletRequest request, @PathVariable Long  idPayment, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> editCreditCardSetting");	
		this.getUrlBack(model, request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();	
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		this.settingPayment(model);
		for (Payment payment :accountSession.getListPayment()){
			if(idPayment.equals(payment.getIdPayment())){
				model.addAttribute("payment", payment );
				break;
			}
		}
		model.addAttribute("provenienza", provenienza);
		logger.info("END WebAccountsController --> editCreditCardSetting");	
		return "editPayment";
	}
	
	@RequestMapping(value="/saveEditCreditCard/{idPayment}/{provenienza}", method=RequestMethod.POST)
	public String saveEditCreditCard(Model model, @ModelAttribute("payment") Payment payment,  @PathVariable Long idPayment, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> saveEditCreditCard");	
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			if(payment.getExpirationYear() < year){
				this.settingPayment(model);
				model.addAttribute("errorDate", Boolean.TRUE);
				model.addAttribute("payment", payment);
				logger.info("END WebAccountsController --> saveEditCreditCard");	
				return "editPayment";
			}else{
				if( payment.getExpirationYear() == year && new Integer(payment.getExpirationMonth()) < month){
					this.settingPayment(model);
					model.addAttribute("errorDate", Boolean.TRUE);
					model.addAttribute("payment", payment);
					logger.info("END WebAccountsController --> saveEditCreditCard");	
					return "editPayment";
				}
			}		
			model.addAttribute("provenienza", provenienza);
			payment.setIdPayment(idPayment);
			this.savePayment(model, payment);	
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> saveEditCreditCard: "+e.getMessage());	
			model.addAttribute("erroreSave", Boolean.TRUE);
			model.addAttribute("payment", payment);
			return "editPayment";			
		}
		model.addAttribute("editPayment", Boolean.TRUE);
		model.addAttribute("noPaymentList",Boolean.FALSE);
		logger.info("END WebAccountsController --> saveEditCreditCard");	
		if(provenienza.equals("cart")){
			return "redirect:/checkout";
		}	

		if(provenienza.equals("gift")){
			return "redirect:/viewGiftCard";
		}
		return "managePaymentOption";
	}
	
	//  ----------------------------------------------------------------
	//  -------------------------GIFT CARD---------------------------------
	//  ----------------------------------------------------------------

	@RequestMapping(value="/viewGiftCard", method=RequestMethod.GET)
	public String viewGiftCard(Model model, HttpServletRequest  request){
		
		logger.info("START WebAccountsController --> viewGiftCard");	
		this.getUrlBack(model, request);
		BigDecimal yourBalanceTot = BigDecimal.ZERO;
		model.addAttribute("path", "/account");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		if(!accountSession.getListGiftCard().isEmpty()){
			for(GiftCard giftCard: accountSession.getListGiftCard()){
				yourBalanceTot = yourBalanceTot.add(giftCard.getBalanceAvailabled());
			}
		}
		model.addAttribute("account", accountSession);
		model.addAttribute("yourBalanceTot", yourBalanceTot);
		logger.info("END WebAccountsController --> viewGiftCard");	
		return "viewGiftCard";	
	}
	
	@RequestMapping(value="/addGiftCard/{provenienza}")
	public String addGiftCard(Model model, HttpServletRequest request, @PathVariable String  provenienza){
		
		logger.info("START WebAccountsController --> addGiftCard");	
		GiftCard giftCard = new GiftCard();
		this.getUrlBack(model, request);
		model.addAttribute("giftCard", giftCard);
		model.addAttribute("provenienza", provenienza);
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		return "addGiftCard";	
	}
	

	@RequestMapping(value="/saveNewGiftCard/{provenienza}", method=RequestMethod.POST)
	public String saveNewGiftCard(Model model, @ModelAttribute("giftCard") GiftCard giftCard, @PathVariable String  provenienza) {
		
		logger.info("START WebAccountsController --> saveNewGiftCard");	
		if(provenienza.equals("cart")){
			model.addAttribute("path", "/cart");
		}else{
			model.addAttribute("path", "/account");
		}
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Account accountSession = (Account) auth.getDetails();
			
			for(GiftCard giftCardSession : accountSession.getListGiftCard()){
				if(giftCardSession.getCode().equals(giftCard.getCode())){
					model.addAttribute("codeError", Boolean.TRUE);
					return "addGiftCard";
				}
			}
			model.addAttribute("provenienza", provenienza);
			this.saveGiftCard(model, giftCard);
			
		}catch (RestClientException e){
			logger.info("ERROR WebAccountsController --> saveNewGiftCard: "+e.getMessage());	
			model.addAttribute("erroreSave", Boolean.TRUE);
			this.settingPayment(model);
			return "addGiftCard";		
		}
		logger.info("END WebAccountsController --> saveNewGiftCard");	
		if(provenienza.equals("cart")){
			return "redirect:/cart";
		}	
		return "redirect:/viewGiftCard";
	}
	//  ------------------------------------------------------------------------------------------------------------
	//  -------------------------PRIVATE METHOD---------------------------------------------------------------------
	//  ------------------------------------------------------------------------------------------------------------	
	private void savePassword(String psw, Model model) {
		
		Login login = new Login();	
		String pswCrypto = this.cryptoPassword(psw);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		login.setIdAccount(accountSession.getUsername());
		Login loginFind = loginService.findLoginByAccount(login);  
		accountSession.setPassword(pswCrypto);			
		Account accountResult = accountsService.updateAccount(accountSession);
	
		loginFind.setIdAccount(accountResult.getUsername());
		loginFind.setPassword(pswCrypto);
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    loginFind.setModifyDate(sqldate);
	    loginService.save(loginFind);
	    accountSession.setPassword(accountResult.getPassword());
		model.addAttribute("account", accountResult);
	}
	
	private void saveAddress(Model model, Address address){
		//check if idAddress is value
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    address.setModifyDate(sqldate);
	    address.setFlagActive("Y");
	    
		if(address.getIdAddress() != null){
			for(Address addressSession: accountSession.getListAddress()){
				if(addressSession.getIdAddress().equals(address.getIdAddress())){			
					addressSession.setAddress1(address.getAddress1());
					addressSession.setAddress2(address.getAddress2());
					addressSession.setCity(address.getCity());
					addressSession.setFullName(address.getFullName());
					addressSession.setPhoneNumeber(address.getPhoneNumeber());
					addressSession.setProvince(address.getProvince());
					addressSession.setState(address.getState());
					addressSession.setZip(address.getZip());
					break;
				}			
			}
		}else{
		    address.setInsertDate(sqldate);
		    
			if(accountSession.getListAddress() == null){
				List<Address> listAddress = new ArrayList<Address>();
				listAddress.add(address);
				accountSession.setListAddress(listAddress);
			}else{
				accountSession.getListAddress().add(address);
			}	
		}
		Account accountResponse = accountsService.updateAccount(accountSession);
		//vado a settare la nuava lista indirizzi
		accountSession.setListAddress(accountResponse.getListAddress());
		model.addAttribute("account", accountResponse);
		model.addAttribute("noAddressList",Boolean.FALSE);
	}
	
	private void settingPayment(Model model){
		
		Payment paymentNew = new Payment();
		List<String> listMonths = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
		Calendar currenttime = Calendar.getInstance();
		Integer year =currenttime.get(Calendar.YEAR);
		List<Integer> listYear = new ArrayList<Integer>();		
		for(int i=year ; i< year+10; i++){
			listYear.add(i);
		}		
		model.addAttribute("listMonths", listMonths);
		model.addAttribute("listYear", listYear);
		model.addAttribute("payment", paymentNew);
	}
	
	private void savePayment(Model model, Payment payment){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    payment.setModifyDate(sqldate);
		if(payment.getIdPayment() != null){
			for(Payment paymentSession: accountSession.getListPayment()){
				if(paymentSession.getIdPayment().equals(payment.getIdPayment())){			
					paymentSession.setExpirationMonth(payment.getExpirationMonth());
					paymentSession.setExpirationYear(payment.getExpirationYear());
					paymentSession.setModifyDate(payment.getModifyDate());
					paymentSession.setName(payment.getName());
					paymentSession.setNumber(payment.getNumber());
					break;
				}		
			}
		}else{
		    payment.setInsertDate(sqldate);
		    payment.setFlagActive("Y");
			if(accountSession.getListPayment() == null){
				List<Payment> listPayment = new ArrayList<Payment>();
				listPayment.add(payment);
				accountSession.setListPayment(listPayment);
			}else{
				accountSession.getListPayment().add(payment);
			}
		}	    
		Account accountResponse = accountsService.updateAccount(accountSession);
		accountSession.setListPayment(accountResponse.getListPayment());
		model.addAttribute("account", accountResponse);
		model.addAttribute("noPaymentList",Boolean.FALSE);
	}
	
	private void getHomeProduct(Model model) throws InterruptedException, ExecutionException {
		model.addAttribute("path", "/");
		List<Category> categories = categoriesService.findAll();
		model.addAttribute("categories", categories);
		List<Product> products = productsService.findProductsRandom();
		List<Item> items = itemsService.findItemsRandomByIdProduct(products.get(0).getProductId());
		CompletableFuture<List<Item>> itemsRecommended = itemsService.findItemsRandom();
		List<Item> featuresItems = itemsService.findFeaturesItemRandom();
		
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		model.addAttribute("items", items);
		model.addAttribute("itemsRecommended", itemsRecommended.get());
		model.addAttribute("featuresItems", featuresItems);
	}
	
	private void saveGiftCard(Model model, GiftCard giftCard){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Account accountSession = (Account) auth.getDetails();
		Calendar currenttime = Calendar.getInstance();
	    Date sqldate = new Date((currenttime.getTime()).getTime());
	    if(giftCard.getBalanceAvailabled() == null){
	    	giftCard.setBalanceAvailabled(giftCard.getBalanceTot());
	    	giftCard.setBalanceUsed(BigDecimal.ZERO);
	    }
	    giftCard.setInsertDate(sqldate);
	    giftCard.setModifyDate(sqldate);
		if(accountSession.getListGiftCard() == null){
			List<GiftCard> listGiftCard = new ArrayList<GiftCard>();
			listGiftCard.add(giftCard);
			accountSession.setListGiftCard(listGiftCard);
		}else{
			accountSession.getListGiftCard().add(giftCard);
		}
		Account accountResponse = accountsService.updateAccount(accountSession);
		accountSession.setListGiftCard(accountResponse.getListGiftCard());
		model.addAttribute("account", accountResponse);
	}
	
	//button back
	private void getUrlBack(Model model, HttpServletRequest request){
		String referer = request.getHeader("Referer");
		String[] value = referer.split("/");
		String urlBack = value[3];
		String address = "";

		if(urlBack.equals("saveNewAddress") || urlBack.equals("saveEditAddress") || urlBack.equals("deleteAddressSetting") || urlBack.equals("addAddressSetting") ){
			 address = value[4];
			if(address.equals("address")){
				urlBack= "manageAddressBook";
			}else{
				if(address.equals("cart")){
					urlBack="checkout";
				}else if(value[5] != null &&  value[5].equals("address") ){
					urlBack= "manageAddressBook";
				}
				
			}
		}
		if(urlBack.equals("addAddressBook")){
			urlBack="checkout";
		}
		if(urlBack.equals("deleteCreditCardSetting") || urlBack.equals("saveEditCreditCard")){
			if(value[4].equals("address")){
				urlBack= "managePaymentOption";
			}else{
				if(address.equals("cart")){
					urlBack="checkout";
				}else if(value[5] != null &&  value[5].equals("address") ){
					urlBack= "managePaymentOption";
				}		
			}
		}
		if(urlBack.equals("saveNewPayment")){
			 address = value[4];
			if(address.equals("payment")){
				urlBack= "managePaymentOption";
			}else if(address.equals("address")){
				urlBack= "manageAddressBook";
			}else{
				urlBack="checkout";
			}
		}
		

		
		model.addAttribute("urlBack", urlBack);
	}


	//method to crypt password
	private String cryptoPassword(String password){


        MessageDigest md;
        StringBuffer sb = new StringBuffer();
        byte[] byteData = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
	        md.update(password.getBytes());
	        byteData = md.digest();
	        //convert the byte to hex format method 1        
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }	
		} catch (NoSuchAlgorithmException e) {
			logger.info("ERROR WebAccountsController --> cryptoPassword: "+e.getMessage());
		}
	
		return sb.toString();
	}

}