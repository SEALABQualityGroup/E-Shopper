package it.univaq.ing.security;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import it.univaq.ing.security.model.Account;
import it.univaq.ing.security.services.WebSecurityService;

/**
 * 
 * @author LC
 *
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	protected WebSecurityService securityService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
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
}
