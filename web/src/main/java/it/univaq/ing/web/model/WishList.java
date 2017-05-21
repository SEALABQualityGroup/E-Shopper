package it.univaq.ing.web.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author LauraCococcia
 */
public class WishList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idWishlist;
	private Long  idItem;
	private String idAccount;
	private Date insertDate;
	private Date modifyDate;
	private Item item;
	
	
	public Long getIdWishlist() {
		return idWishlist;
	}
	public void setIdWishlist(Long idWishlist) {
		this.idWishlist = idWishlist;
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
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}