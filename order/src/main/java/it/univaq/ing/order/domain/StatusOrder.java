package it.univaq.ing.order.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LC
 */
@Entity
@Table(name="STATUS_ORDER")
public class StatusOrder {
	
	@Id
	public Integer idStatus ;	
	public String description;
	
	public Integer getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(Integer idStatus) {
		this.idStatus = idStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
