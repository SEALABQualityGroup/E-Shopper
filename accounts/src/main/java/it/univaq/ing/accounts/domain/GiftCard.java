package it.univaq.ing.accounts.domain;

import java.math.BigDecimal;
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
@Table(name="GIFT_CARD")
public class GiftCard {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idGift= 0l;
	
	private String code;
	private BigDecimal balanceTot;
	private BigDecimal balanceAvailabled;
	private BigDecimal balanceUsed;
	private Date insertDate;
	private Date modifyDate;
	
	public Long getIdGift() {
		return idGift;
	}
	public void setIdGift(Long idGift) {
		this.idGift = idGift;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getBalanceTot() {
		return balanceTot;
	}
	public void setBalanceTot(BigDecimal balanceTot) {
		this.balanceTot = balanceTot;
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
	public BigDecimal getBalanceAvailabled() {
		return balanceAvailabled;
	}
	public void setBalanceAvailabled(BigDecimal balanceAvailabled) {
		this.balanceAvailabled = balanceAvailabled;
	}
	public BigDecimal getBalanceUsed() {
		return balanceUsed;
	}
	public void setBalanceUsed(BigDecimal balanceUsed) {
		this.balanceUsed = balanceUsed;
	}		
}
