package com.sip.ams.entities;

public class Candidat {
	public int id;
	public String nom;
	public String email;
	public String telephone;
	
	public Candidat(int id, String nom, String email, String telephone) {
		this.id = id;
		this.nom = nom;
		this.email = email;
		this.telephone = telephone;
	}
}
