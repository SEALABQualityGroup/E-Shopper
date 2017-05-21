package it.univaq.ing.security;


import java.io.Serializable;
import java.security.Principal;

/**
 * 
 * @author LC
 *
 */

public class UserPrincipal implements Principal, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;

	public UserPrincipal() {
	}
	
	public UserPrincipal(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String getName() {
		return getUsername();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
