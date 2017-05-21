package it.univaq.ing.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * 
 * @author LauraCococcia
 */
public class CartItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idCart;	
	private String flagOrder = "N";
	private String idAccount;	
	private Long  idItem;	
	private BigDecimal price;
	private int quantity;
	private Item item;
	private Date insertDate;
	private Date modifyDate;
	
	public Long getIdCart() {
		return idCart;
	}
	public void setIdCart(Long idCart) {
		this.idCart = idCart;
	}
	public String getFlagOrder() {
		return flagOrder;
	}
	public void setFlagOrder(String flagOrder) {
		this.flagOrder = flagOrder;
	}
	public Long getIdItem() {
		return idItem;
	}
	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public String getIdAccount() {
		return idAccount;
	}
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
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