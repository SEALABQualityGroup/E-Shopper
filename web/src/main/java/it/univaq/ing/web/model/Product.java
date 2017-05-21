package it.univaq.ing.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author LC
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId = 0l;	
	private String name;
	private String description;	
	private Long categoryId;
	private List<Item> listItem = new ArrayList<Item>();
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public List<Item> getListItem() {
		return listItem;
	}
	public void setListItem(List<Item> listItem) {
		this.listItem = listItem;
	}
}