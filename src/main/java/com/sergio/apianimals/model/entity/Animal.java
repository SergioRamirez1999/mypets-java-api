package com.sergio.apianimals.model.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="animals")
public class Animal  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ANIMAL")
	private Long id;
	
	@Column(name="NAME_ANIMAL", nullable=false, length=30)
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(name="BIRTHDATE_ANIMAL", nullable=false)
	private Date birthdate;
	
	@Column(name="WEIGHT_ANIMAL", nullable=false)
	private Double weight;
	
	@Column(name="TYPE_ANIMAL", nullable=false)
	@Enumerated(EnumType.STRING)
	private AnimalType animalType;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ID_GROUP")
	private FamilyGroup familyGroup;

	public Animal() {
	}

	public Animal(String name, Date birthdate, Double weight, AnimalType animalType, FamilyGroup familyGroup) {
		this.name = name;
		this.birthdate = birthdate;
		this.weight = weight;
		this.animalType = animalType;
		this.familyGroup = familyGroup;
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public AnimalType getAnimalType() {
		return animalType;
	}

	public void setAnimalType(AnimalType animalType) {
		this.animalType = animalType;
	}

	public FamilyGroup getFamilyGroup() {
		return familyGroup;
	}

	public void setFamilyGroup(FamilyGroup familyGroup) {
		this.familyGroup = familyGroup;
	}

	@Override
	public String toString() {
		return "Animal [id=" + id + ", name=" + name + ", birthdate=" + birthdate + ", weight=" + weight
				+ ", animalType=" + animalType + ", familyGroup=" + familyGroup + "]";
	}
	

}
