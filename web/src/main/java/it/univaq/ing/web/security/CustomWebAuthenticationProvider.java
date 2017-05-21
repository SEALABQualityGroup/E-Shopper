package it.univaq.ing.web.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import it.univaq.ing.security.UserPrincipal;
import it.univaq.ing.security.model.Account;
import it.univaq.ing.security.services.WebSecurityService;

/**
 * 
 * @author LC
 *
 */

@Component
public class CustomWebAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	protected WebSecurityService securityService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
		String password = this.cryptoPassword(authentication.getCredentials().toString());
		
		Account account = new Account();
		account.setUsername(name);
		account.setEmail(name);
		account.setPassword(password);
		//Check the db that the user is valid, otherwise returns null
		Account signed = securityService.signin(account);
		if(signed == null){
			return null;
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserPrincipal(name,password), password, new ArrayList<>());
		token.setDetails(signed);
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return sb.toString();
	}
}
