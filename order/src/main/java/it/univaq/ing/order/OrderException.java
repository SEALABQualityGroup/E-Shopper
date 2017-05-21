package it.univaq.ing.order;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

/**
 * 
 * @author LC
 */
public class OrderException extends RestClientResponseException{

	private static final long serialVersionUID = 1L;
	
	public OrderException(String message, int statusCode) {
		super(message, statusCode, message, null, null, Charset.defaultCharset());
	}

	public OrderException(String message, int statusCode, String statusText, HttpHeaders responseHeaders,
			byte[] responseBody, Charset responseCharset) {
		super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}
}
