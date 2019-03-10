package com.sergio.apianimals.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_USER")
	private Long id;
	
	@Column(name="NAME_USER", nullable=false, length=30)
	private String name;
	
	@Column(name="LASTNAME_USER", nullable=false, length=30)
	private String lastName;
	
	@Column(name="EMAIL_USER", nullable=false, unique=true, length=40)
	private String email;
	
	@Column(name="PASSWORD_USER", nullable=false, length=60)
	private String password;
	
	@Column(name="ENABLED_USER", nullable=false)
	private Boolean enabled = false;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "ID_GROUP")
	private FamilyGroup familyGroup;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="USER_ID")
	private List<Authority> authorities = null;
	
	@Column(name="IMAGE_USER")
	private String image;

	public User() {
	}

	public User(String name, String lastName, String email, String password, Boolean enabled, FamilyGroup familyGroup,
			List<Authority> authorities, String image) {
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.familyGroup = familyGroup;
		this.authorities = authorities;
		this.image = image;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public FamilyGroup getFamilyGroup() {
		return familyGroup;
	}

	public void setFamilyGroup(FamilyGroup familyGroup) {
		this.familyGroup = familyGroup;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + ", enabled=" + enabled + ", familyGroup=" + familyGroup + ", authorities="
				+ authorities + ", image=" + image + "]";
	}

}
