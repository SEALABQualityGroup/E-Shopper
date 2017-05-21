package it.univaq.ing.order.domain;


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
@Table(name="REL_CART_ORDER")
public class RelCartOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idRel = 0l;
	
	private Long idCart;
	private Date insertDate;
	private Date modifyDate;
	
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
}
