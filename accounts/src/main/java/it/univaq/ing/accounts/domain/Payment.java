package it.univaq.ing.accounts.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LC
 */
@Entity
@Table(name="PAYMENT")
public class Payment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idPayment= 0l;
	
	private String name;
	private String number;
	private String expirationMonth;
	private Integer expirationYear;
	private Date insertDate;
	private Date modifyDate;
	private String flagActive;
		
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
	
}
