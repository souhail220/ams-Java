package com.sip.ams.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Provider {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message = "Name is mandatory")
	@Column(name = "name")
	private String name;
	
	

	@NotBlank(message = "Adress is mandatory")
	@Column(name = "adress")
	private String adress;
	
	@NotBlank(message = "Email is mandatory")
	@Column(name = "email")
	private String email;
	
	@Column(name ="logo")
	private String logo;
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Provider() {}
	
	public Provider(String name, String adress, String email, String logo) {
		this.name = name;
		this.adress = adress;
		this.email = email;
		this.logo = logo;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
