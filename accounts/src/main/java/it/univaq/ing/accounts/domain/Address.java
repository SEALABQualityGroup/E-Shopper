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
@Table(name="ADDRESS")
public class Address {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idAddress = 0l;
	private String fullName;
	private String address1;
	private String address2;
	private String city;
	private String province;
	private String state;
	private String zip;
	private Date insertDate;
	private Date modifyDate;
	private String phoneNumeber;
	private String flagActive;

	public Long getIdAddress() {
		return idAddress;
	}
	
	public void setIdAddress(Long idAddress) {
		this.idAddress = idAddress;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getPhoneNumeber() {
		return phoneNumeber;
	}
	
	public void setPhoneNumeber(String phoneNumeber) {
		this.phoneNumeber = phoneNumeber;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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
