package it.univaq.ing.security;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
/**
 * 
 * @author LC
 *
 */

public class CustomCredentialProvider extends BasicCredentialsProvider {

	public CustomCredentialProvider() {
		super();		
		Credentials credentials = new it.univaq.ing.security.Credentials();
		this.setCredentials(AuthScope.ANY, credentials );
	}

}
