package com.sip.ams;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sip.ams.controllers.ArticleController;

@SpringBootApplication
public class AmsApplication {

	public static void main(String[] args) {
		new File(ArticleController.uploadDirectory).mkdir();
		SpringApplication.run(AmsApplication.class, args);
	}

}
