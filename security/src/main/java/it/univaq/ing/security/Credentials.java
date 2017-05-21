package it.univaq.ing.security;

import java.security.Principal;

import org.springframework.security.core.context.SecurityContextHolder;
/**
 * 
 * @author LC
 *
 */
public class Credentials implements org.apache.http.auth.Credentials {

	public Credentials() {
	}

	@Override
	public Principal getUserPrincipal() {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Principal) {
			return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		return new UserPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),"");
	}

	@Override
	public String getPassword() {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal){
			return ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPassword();
		}
		return null;
	}

}
