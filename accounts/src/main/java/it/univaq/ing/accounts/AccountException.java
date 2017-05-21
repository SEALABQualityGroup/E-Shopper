package it.univaq.ing.accounts;


import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

/**
 * Custom exception
 * 
 * @author LC
 */
public class AccountException extends RestClientResponseException {

	private static final long serialVersionUID = 1L;
	
	public AccountException(String message, int statusCode) {
		super(message, statusCode, message, null, null, Charset.defaultCharset());
	}

	public AccountException(String message, int statusCode, String statusText, HttpHeaders responseHeaders,
			byte[] responseBody, Charset responseCharset) {
		super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}


}
