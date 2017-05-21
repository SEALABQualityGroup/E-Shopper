package it.univaq.ing.cart;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

/**
 * Custom exception
 * 
 * @author LC
 */
public class CartException extends RestClientResponseException{

	private static final long serialVersionUID = 1L;
	
	public CartException(String message, int statusCode) {
		super(message, statusCode, message, null, null, Charset.defaultCharset());
	}

	public CartException(String message, int statusCode, String statusText, HttpHeaders responseHeaders,
			byte[] responseBody, Charset responseCharset) {
		super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}

	
}
