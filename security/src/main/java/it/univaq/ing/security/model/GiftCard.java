package it.univaq.ing.security.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 
 * @author LC
 */
public class GiftCard {

	private Long idGift;
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
