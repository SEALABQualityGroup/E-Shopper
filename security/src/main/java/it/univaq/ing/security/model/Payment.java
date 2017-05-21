package it.univaq.ing.security.model;

import java.sql.Date;
import java.util.Calendar;

/**
 * 
 * @author LC
 */
public class Payment {

	private Long idPayment;
	private String name;
	private String number;
	private String expirationMonth;
	private Integer expirationYear;
	private Date insertDate;
	private Date modifyDate;
	private String flagActive;
	private Boolean creditCardExpire;
	private String numberCript;
		
	public Long getIdPayment() {
		return idPayment;
	}		
	public void setIdPayment(Long idPayment) {
		this.idPayment = idPayment;
	}		
	public String getName() {
		return name;
	}		
	public void setName(String name) {
		this.name = name;
	}	
	public String getNumber() {
		return number;
	}	
	public void setNumber(String number) {
		this.number = number;
	}	
	public String getExpirationMonth() {
		return expirationMonth;
	}
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	public Integer getExpirationYear() {
		return expirationYear;
	}
	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}
	public Date getInsertDate() {
		return insertDate;
	}		
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}		
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	public Boolean getCreditCardExpire() {
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		if(getExpirationYear() < year){
			creditCardExpire = Boolean.TRUE;
			return creditCardExpire ;
		}else{
			if( getExpirationYear() == year && new Integer(getExpirationMonth()) < month){
				creditCardExpire = Boolean.TRUE;
				return creditCardExpire;
			}
		creditCardExpire = Boolean.FALSE;
		return creditCardExpire;
		}
	}

	public void setCreditCardExpire(Boolean creditCardExpire) {
		this.creditCardExpire = getCreditCardExpire();
	}
	public String getNumberCript() {
		 numberCript = "****" + getNumber().substring(12);
		return numberCript;
	}
	public void setNumberCript(String numberCript) {
		this.numberCript = numberCript;
	}	
	
	
}
