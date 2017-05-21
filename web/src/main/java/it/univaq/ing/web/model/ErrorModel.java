package it.univaq.ing.web.model;

/**
 * 
 * @author LC
 *
 */
public class ErrorModel {

	int status;
	String message;
	
	public ErrorModel() {
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
