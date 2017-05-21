package it.univaq.ing.wishlist;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

/**
 * 
 * @author LC
 *
 */
public class WishListException extends RestClientResponseException{


	private static final long serialVersionUID = 1L;
	
	public WishListException(String message, int statusCode) {
		super(message, statusCode, message, null, null, Charset.defaultCharset());
	}

	public WishListException(String message, int statusCode, String statusText, HttpHeaders responseHeaders,
			byte[] responseBody, Charset responseCharset) {
		super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}	
}
