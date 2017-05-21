package it.univaq.ing.web.model;


import java.sql.Date;

/**
 * 
 * @author Laura Cococcia
 */
public class RelCartOrder {
	
	private Long idRel = 0l;
	private Long idCart;
	private Date insertDate;
	private Date modifyDate;
	private CartItem cart;
	
	public Long getIdRel() {
		return idRel;
	}
	public void setIdRel(Long idRel) {
		this.idRel = idRel;
	}

	public Long getIdCart() {
		return idCart;
	}
	public void setIdCart(Long idCart) {
		this.idCart = idCart;
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
	public CartItem getCart() {
		return cart;
	}
	public void setCart(CartItem cart) {
		this.cart = cart;
	}	
}
