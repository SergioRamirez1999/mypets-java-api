package com.sergio.apianimals.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="foods")
public class Food implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_FOOD", nullable=false)
	private Long id;
	
	@Column(name="ID_NAME", length=30, nullable=false)
	private String name;
	
	@Column(name="ID_QUANTITY", nullable=false)
	private Double quantity;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TYPE_FOOD", nullable=false)
	private FoodType type;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ID_GROUP")
	private Inventory inventory;
	

	public Food() {
	}


	public Food(String name, Double quantity, FoodType type, Inventory inventory) {
		this.name = name;
		this.quantity = quantity;
		this.type = type;
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


	public FoodType getType() {
		return type;
	}


	public void setType(FoodType type) {
		this.type = type;
	}


	public Inventory getInventory() {
		return inventory;
	}


	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}


	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + ", quantity=" + quantity + ", type=" + type + ", inventory="
				+ inventory + "]";
	}
	

}
