package com.teste.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.teste.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmation(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
