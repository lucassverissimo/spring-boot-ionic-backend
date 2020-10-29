package com.verissimoLucas.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.verissimoLucas.cursomc.domain.Cliente;
import com.verissimoLucas.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
