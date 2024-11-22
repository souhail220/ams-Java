package com.sip.ams.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ProviderRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/provider/")
public class ProviderController {
	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

	private final ProviderRepository providerRepository;

	public ProviderController(ProviderRepository providerRepository) {
		this.providerRepository = providerRepository;
	}

	@GetMapping("list")
	public String listProviders(Model model) {
		List<Provider> lp = (List<Provider>) providerRepository.findAll();
		model.addAttribute("providers", lp.size() == 0 ? null : lp);

		return "provider/listProviders";
	}

	@GetMapping("add")
	public String addProviderForm(Model model) {

		Provider provider = new Provider();
		model.addAttribute("provider", provider);
		return "provider/addProvider";
	}

	@PostMapping("add")
	public String saveProvider(@Valid Provider provider, BindingResult result) {

		if (result.hasErrors()) {
			logger.info("Hamma");
			return "provider/addProvider";
		}
		providerRepository.save(provider);
		return "redirect:list";
	}

	@GetMapping("delete/{id}")
	public String deleteProvider(@PathVariable long id, Model model) {

		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		providerRepository.delete(provider);
		return "redirect:../list";
	}
	
	@GetMapping("edit/{id}")
	public String editProvider(@PathVariable long id, Model model) {
		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		model.addAttribute("provider", provider);
		
		return "provider/updateProvider";
	}
	
	@PostMapping("update")
	public String updateProvider(@Valid Provider provider, BindingResult result) {
		
		if(result.hasErrors()) {
			return "provider/updateProvider";
		}
		providerRepository.save(provider);
		return "redirect:list";
	}

}