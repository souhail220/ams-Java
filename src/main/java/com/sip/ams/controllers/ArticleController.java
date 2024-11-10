package com.sip.ams.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sip.ams.entities.Article;
import com.sip.ams.repositories.ArticleRepository;
import com.sip.ams.repositories.ProviderRepository;

@Controller
@RequestMapping("/article/")
public class ArticleController {
	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);
	
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	
	@Autowired
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
}
