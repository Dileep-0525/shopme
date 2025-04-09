package com.dileep.shopme.common.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer role_id;
	@Column(name = "role_name", nullable = false , unique = true ,length = 40)
	private String role_name;
	@Column(name = "description" , nullable = false , length = 150)
	private String description;
	
	public Role() {
		
	}
	
	
	
	public Role(Integer role_id) {
		super();
		this.role_id = role_id;
	}



	public Role(String role_name) {
		this.role_name = role_name;
	}
	
	public Role( String role_name, String description) {
		this.role_name = role_name;
		this.description = description;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



	@Override
	public int hashCode() {
		return Objects.hash(role_id);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(role_id, other.role_id);
	}



	@Override
	public String toString() {
		return this.role_name;
	}
	
	
	
}
