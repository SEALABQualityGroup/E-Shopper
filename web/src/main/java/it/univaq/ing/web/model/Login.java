package it.univaq.ing.web.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author LC
 *
 */
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String idAccount;	
	private String password;
	private Date insertDate;
	private Date modifyDate;
	
	public String getIdAccount() {
		return idAccount;
	}
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
}