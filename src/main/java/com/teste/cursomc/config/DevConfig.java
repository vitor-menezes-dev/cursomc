package com.teste.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.teste.cursomc.services.DBService;
import com.teste.cursomc.services.EmailService;
import com.teste.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDatabase() throws ParseException {
		System.out.println(strategy);
		if (strategy.contains("create"))
			dbService.instantieteTestDatabase();
		return true;
	}

	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
}
