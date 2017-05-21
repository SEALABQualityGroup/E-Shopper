package it.univaq.ing.categories;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;

/**
 * 
 * @author LC
 */
public class CategoryException extends RestClientResponseException {

	private static final long serialVersionUID = 1L;
	
	public CategoryException(String message, int statusCode) {
		super(message, statusCode, message, null, null, Charset.defaultCharset());
	}

	public CategoryException(String message, int statusCode, String statusText, HttpHeaders responseHeaders,
			byte[] responseBody, Charset responseCharset) {
		super(message, statusCode, statusText, responseHeaders, responseBody, responseCharset);
	}

}
