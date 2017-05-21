package it.univaq.ing.web.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * 
 * @author Laura Cococcia
 */
public class Order {
	
	private Long idOrder;
	private String code ;
	private BigDecimal totPrice;
	private StatusOrder idStatus;
	private String idAccount;	
	private Long idAddress;	
	private Long idPayment;	
	private Long idGift = 0l;
	private List<RelCartOrder> listRelCartOrder;	
	private String note;	
	private Date insertDate;
	private Date modifyDate;
	private String addressSmall;
	private String codeGiftCard;
	private String codePayment;
	
	public Long getIdOrder() {
		return idOrder;
	}	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}
	public BigDecimal getTotPrice() {
		return totPrice;
	}
	public void setTotPrice(BigDecimal totPrice) {
		this.totPrice = totPrice;
	}
	
	public StatusOrder getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(StatusOrder idStatus) {
		this.idStatus = idStatus;
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
	public String getIdAccount() {
		return idAccount;
	}
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<RelCartOrder> getListRelCartOrder() {
		return listRelCartOrder;
	}
	public void setListRelCartOrder(List<RelCartOrder> listRelCartOrder) {
		this.listRelCartOrder = listRelCartOrder;
	}
	public Long getIdAddress() {
		return idAddress;
	}
	public void setIdAddress(Long idAddress) {
		this.idAddress = idAddress;
	}
	public Long getIdPayment() {
		return idPayment;
	}
	public void setIdPayment(Long idPayment) {
		this.idPayment = idPayment;
	}
	public Long getIdGift() {
		return idGift;
	}
	public void setIdGift(Long idGift) {
		this.idGift = idGift;
	}
	public String getAddressSmall() {
		return addressSmall;
	}
	public void setAddressSmall(String addressSmall) {
		this.addressSmall = addressSmall;
	}
	public String getCodeGiftCard() {
		return codeGiftCard;
	}
	public void setCodeGiftCard(String codeGiftCard) {
		this.codeGiftCard = codeGiftCard;
	}
	public String getCodePayment() {
		return codePayment;
	}
	public void setCodePayment(String codePayment) {
		this.codePayment = codePayment;
	}	
}