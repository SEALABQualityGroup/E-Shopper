package it.univaq.ing.order.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author LC
 */
@Entity
@Table(name="ORDER")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idOrder =  0l;
	
	private String code ;
	private BigDecimal totPrice;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private StatusOrder idStatus;

	private String idAccount;	
	private Long idAddress;	
	private Long idPayment;
	private Long idGift;
	
	@OneToMany(cascade = CascadeType.ALL)	
	@JoinColumn(name="ID_ORDER")
	private List<RelCartOrder> listRelCartOrder;
	
	private String note;
	private Date insertDate;	
	private Date modifyDate;
	
	
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
	
}
