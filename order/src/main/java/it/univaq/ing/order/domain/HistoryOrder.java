package it.univaq.ing.order.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author LC
 *
 */
@Entity
@Table(name="HISTORY_ORDER")
public class HistoryOrder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer idHistory = 0;
	
	@OneToOne
	@JoinColumn(name="ID_ORDER")
	public Order order;
	
	@OneToOne
	@JoinColumn(name="ID_STATUS")
	public StatusOrder statusOrder;
	
	public Date statusDate;
	public Date insertDate;
	public Date modifyDate;
	
	public Integer getIdHistory() {
		return idHistory;
	}
	public void setIdHistory(Integer idHistory) {
		this.idHistory = idHistory;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public StatusOrder getStatusOrder() {
		return statusOrder;
	}
	public void setStatusOrder(StatusOrder statusOrder) {
		this.statusOrder = statusOrder;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
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
