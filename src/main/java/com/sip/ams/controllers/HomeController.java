package com.sip.ams.controllers;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sip.ams.entities.Candidat;

@Controller
public class HomeController {
	
	static ArrayList<Candidat> lc;
	static {
		 lc = new ArrayList<>();
		Candidat c1 = new Candidat(0,"Naouel","naouel@gmail.com","11111111");
		Candidat c2 = new Candidat(1,"Sameh","samed@gmail.com","22222222");
		Candidat c3 = new Candidat(2,"Amine","amine@gmail.com","33333333");
		lc.add(c1);
		lc.add(c2);
		lc.add(c3);
	}
	
	
	@GetMapping("/candidats")
	//@ResponseBody
	public String listCandidats(Model m) {
		
		String libelleFormation ="Spring Boot & Angular";
		
		m.addAttribute("lf", libelleFormation);
		m.addAttribute("tab",lc);
		return "home/candidate";
	}
	
	@GetMapping("/add")
	public String addCandidate() {
		return "home/add";
	}
	
	@PostMapping("/add")
	//@ResponseBody
	public String saveCandidate(
			@RequestParam int id,
			@RequestParam String nom,
			@RequestParam String email,
			@RequestParam String tel
			) {
		Candidat temp = new Candidat(id, nom, email, tel);
		lc.add(temp);
		//return "infos: " + id + " " + nom + " " + email + " " + tel + "\n";
		return "redirect:candidats";
	}
	
	
	@GetMapping("/show/{id}")
	@ResponseBody
	public String show(@PathVariable int id) {
		
		return "id: " + id; 
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		
		lc.remove(id);
		return "redirect:../candidats"; 
	}
	
}
