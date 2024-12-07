package com.sip.ams.controllers;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

import com.sip.ams.entities.Article;
import com.sip.ams.entities.Provider;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/article/")
public class ArticleController {
	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;

	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
	}

	private String uploadImage(MultipartFile[] files) {
		// picture upload
		StringBuilder fileName = new StringBuilder();
		MultipartFile file = files[0];
		Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());

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
	public String listArticle(Model model) {

		List<Article> articlesList = (List<Article>) articleRepository.findAll();
		model.addAttribute("article", articlesList.size() == 0 ? null : articlesList);

		return "article/listArticles";
	}

	@GetMapping("add")
	public String addArticleForm(Model model) {
		model.addAttribute("article", new Article());
		model.addAttribute("providers", providerRepository.findAll());
		return "article/addArticle";
	}

	@PostMapping("add")
	public String saveArticle(@Valid Article article, BindingResult result,
			@RequestParam(name = "providerId", required = true) Long p,
			@RequestParam(required = false) MultipartFile[] files) {
		if (result.hasErrors()) {
			return "redirect:../add";
		}
		Provider provider = providerRepository.findById(p)
				.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + p));
		article.setProvider(provider);

		// picture upload
		String fileName = uploadImage(files);
		if(!fileName.isEmpty()) {
			article.setPicture(fileName.toString());
		}
		
		articleRepository.save(article);
		return "redirect:list";
	}

	@GetMapping("show/{id}")
	public String showArticle(@PathVariable Long id, Model model) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Article Id: " + id));
		model.addAttribute("article", article);

		return "article/showArticle";
	}

	@GetMapping("edit/{id}")
	public String editArticleForm(@PathVariable Long id, Model model) {
		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("article", article);
		model.addAttribute("providerId", article.getProvider().getId());
		return "article/updateArticle";
	}

	@PostMapping("edit/{id}")
	public String editArticle(
			@RequestParam(name = "providerId", required = true) Long pr_id,
			@Valid Article article,
			@RequestParam(required = false) MultipartFile[] files,
			BindingResult result) {
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> logger.info(error.toString()));
			return "article/updateArticle";
		}
		Provider provider = providerRepository.findById(pr_id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + pr_id));
		
		String fileName = uploadImage(files);
		if(!fileName.isEmpty()) {
			article.setPicture(fileName.toString());
		}
		
		article.setProvider(provider);
		articleRepository.save(article);
		return "redirect:../list";
	}

	@GetMapping("delete/{id}")
	public String deleteArticle(@PathVariable Long id) {

		Article article = articleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		articleRepository.delete(article);
		return "redirect:../list";
	}
}
