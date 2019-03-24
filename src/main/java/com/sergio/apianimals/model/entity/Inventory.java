package com.sergio.apianimals.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="inventories")
public class Inventory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="INVENTORY_ID")
	private Long id;
	
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="inventory")
	private List<Food> foods;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="inventory")
	private List<Medicine> medicines;
	
	@OneToOne
	@JoinColumn(name="ID_GROUP")
	private FamilyGroup family;
	

	public Inventory() {
	}


	public Inventory(List<Food> foods, List<Medicine> medicines, FamilyGroup family) {
		this.foods = foods;
		this.medicines = medicines;
		this.family = family;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public List<Food> getFoods() {
		return foods;
	}


	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}


	public List<Medicine> getMedicines() {
		return medicines;
	}


	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}


	public FamilyGroup getFamily() {
		return family;
	}


	public void setFamily(FamilyGroup family) {
		this.family = family;
	}
	
	
}
