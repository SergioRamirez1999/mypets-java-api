package com.sergio.apianimals.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="authorities" , uniqueConstraints= {@UniqueConstraint(columnNames= {"USER_ID", "ROLE_AUTHORITY"})})
public class Authority implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_AUTHORITY")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ROLE_AUTHORITY")
	private RoleType role;
	
	public Authority() {
	}

	public Authority(RoleType role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Authority [id=" + id +  ", role=" + role + "]";
	}
	

}
