package it.univaq.ing.web.model;

import java.sql.Date;

/**
 * 
 * @author LC
 *
 */
public class HistoryOrder {
	
	public Integer idHistory;
	public Order order;
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