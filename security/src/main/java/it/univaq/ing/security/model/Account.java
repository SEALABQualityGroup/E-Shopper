package it.univaq.ing.security.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * 
 * @author LC
 *
 */
public class Account implements Serializable {
	
	/**
	 * Thanks to serialization, the object is correctly mapped
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String name;
	private String email;
	private BigDecimal balance;
	private Date insertDate;
	private Date modifyDate;
	private String flagActive;
	private List<Address> listAddress;
	private List<Payment> listPayment;
	private List<GiftCard> listGiftCard;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Address> getListAddress() {
		return listAddress;
	}
	public void setListAddress(List<Address> listAddress) {
		this.listAddress = listAddress;
	}
	public List<Payment> getListPayment() {
		return listPayment;
	}
	public void setListPayment(List<Payment> listPayment) {
		this.listPayment = listPayment;
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
	public List<GiftCard> getListGiftCard() {
		return listGiftCard;
	}
	public void setListGiftCard(List<GiftCard> listGiftCard) {
		this.listGiftCard = listGiftCard;
	}
}
