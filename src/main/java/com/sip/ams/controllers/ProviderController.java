package com.sip.ams.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

	private String uploadImage(MultipartFile[] files) {
		// picture upload
		StringBuilder fileName = new StringBuilder();
		MultipartFile file = files[0];
		Path fileNameAndPath = Paths.get(ArticleController.uploadDirectory, file.getOriginalFilename());

		fileName.append(file.getOriginalFilename());
		try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException exception) {
			exception.printStackTrace();
			return "";
		}
		return fileName.toString();
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
	public String saveProvider(@Valid Provider provider, BindingResult result,
			@RequestParam(required = false) MultipartFile[] files) {

		logger.info("Validation error count: " + result.getErrorCount());
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> logger.info(error.toString()));
			return "provider/addProvider";
		}

		// picture upload
		String fileName = uploadImage(files);
		if (!fileName.isEmpty()) {
			provider.setLogo(fileName.toString());
		}

		providerRepository.save(provider);
		logger.info("Provider saved successfully");
		return "redirect:list";

	}

	@GetMapping("delete/{id}")
	public String deleteProvider(@PathVariable long id, Model model) {

		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		
		// delete logo part
		String fileName = provider.getLogo();
		Path fileNameAndPath = Paths.get(ArticleController.uploadDirectory, fileName);
		try {
			Files.delete(fileNameAndPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("deleting error: " + e.getMessage());
			e.printStackTrace();
		}
		
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
	public String updateProvider(@Valid Provider provider, BindingResult result, @RequestParam(required = false) MultipartFile[] files) {
		logger.info("Validation error count: " + result.getErrorCount());
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> logger.info(error.toString()));
			return "provider/updateProvider";
		}

		// picture upload
		String fileName = uploadImage(files);
		if (!fileName.isEmpty()) {
			provider.setLogo(fileName.toString());
		}

		providerRepository.save(provider);
		return "redirect:list";
	}

	@GetMapping("show/{id}")
	public String showProviderDetails(@PathVariable long id, Model model) {

		Provider provider = providerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
		model.addAttribute("provider", provider);
		return "provider/showProvider";
	}
}
