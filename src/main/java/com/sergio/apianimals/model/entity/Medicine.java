package com.sergio.apianimals.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="medicines")
public class Medicine implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_MEDICINE")
	private Long id;
	
	@Column(name="NAME_MEDICINE")
	private String name;
	
	@Column(name="QUANTITY_MEDICINE")
	private Double quantity;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="ID_INVENTORY")
	private Inventory inventory;
	

	public Medicine() {
	}


	public Medicine(String name, Double quantity, Inventory inventory) {
		this.name = name;
		this.quantity = quantity;
		this.inventory = inventory;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Double getQuantity() {
		return quantity;
	}


	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}


	public Inventory getInventory() {
		return inventory;
	}


	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}


	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", quantity=" + quantity + ", inventory=" + inventory + "]";
	}
	
	
}
