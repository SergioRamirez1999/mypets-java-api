package com.sergio.apianimals.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FamilyGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_GROUP")
	private Long id;
	
	@Column(name="TOKEN_GROUP", length=30, unique=true, nullable=false)
	private String token;
	
	public FamilyGroup() {
	}

	public FamilyGroup(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "FamilyGroup [id=" + id + ", token=" + token + "]";
	}

}
