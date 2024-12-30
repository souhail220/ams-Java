package com.sip.ams.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sip.ams.entities.Role;
import com.sip.ams.repositories.RoleRepository;

@Controller
@RequestMapping("/roles/")
public class RoleController {
	
	public RoleRepository roleRepository;
	
	public RoleController(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@GetMapping("list")
	public String rolesList(Model model) {
		
		List<Role> listRoles = roleRepository.findAll();
		
		int nbrRole = listRoles.size();
		if(nbrRole == 0) {
			listRoles = null;
		}
		model.addAttribute("nbr", nbrRole);
		model.addAttribute("roles", listRoles);
		
		return "role/listRoles";
	}
	
	@GetMapping("add")
	public String addRolesForm(Model model) {
		
		return "role/addRoles";
	}
	
	@PostMapping("add")
	public String saveRole(@RequestParam("role") String roleName) {
		
		Role role = new Role(roleName);
		roleRepository.save(role);
		
		return "redirect:list";
		
	}
}
