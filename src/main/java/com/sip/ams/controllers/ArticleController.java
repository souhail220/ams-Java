package com.sip.ams.controllers;

import java.util.List;

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
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	
	public ArticleController(ArticleRepository articleRepository, ProviderRepository providerRepository) {
		this.articleRepository = articleRepository;
		this.providerRepository = providerRepository;
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
	public String saveArticle(@Valid Article article, BindingResult result, @RequestParam(name="providerId", required=true) Long p) {
		if(result.hasErrors()) {
			return "redirect:../add";
		}
		Provider provider = providerRepository.findById(p).orElseThrow(()-> new IllegalArgumentException("Invalid article Id:" + p));
		article.setProvider(provider);
		articleRepository.save(article);
		return "redirect:list";
	}
	
	@GetMapping("edit/{id}")
	public String editArticleForm(@PathVariable Long id, Model model) {
		Article article = articleRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("Invalid article Id:" + id));
		model.addAttribute("providers", providerRepository.findAll());
		model.addAttribute("article", article);
		model.addAttribute("providerId", article.getProvider().getId());
		return "article/updateArticle";
	}
	
	@PostMapping("edit/{id}")
	public String editArticle(
			@RequestParam(name="providerId", required=true) Long pr_id, 
			@PathVariable Long id,
			@Valid Article article,
			BindingResult result,
			Model model) 
	{	
		if(result.hasErrors()) {
			return "article/updateArticle";
		}
		Provider provider = providerRepository.findById(pr_id).orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + pr_id));
		article.setProvider(provider);
		articleRepository.save(article);
		
		return "redirect:../list";
	}
	
	@GetMapping("delete/{id}")
	public String deleteArticle(@PathVariable Long id) {
		
		Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
		articleRepository.delete(article);
		return "redirect:../list";
	}
}
