package com.bkap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer status;
	
	 public Category() {
	    }

	public  Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public  Integer getStatus() {
		return status;
	}

	public Category(Long id, String name, Integer status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
  
}
