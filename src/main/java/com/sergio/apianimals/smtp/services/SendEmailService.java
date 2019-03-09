package com.sergio.apianimals.smtp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sergio.apianimals.smtp.entity.EmailReceiver;

@Service
public class SendEmailService {
	
	private static final String FROM_EMAIL = "dalaser20@hotmail.com";
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendVerificationEmail(EmailReceiver toReceiver, String token) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(FROM_EMAIL);
		simpleMailMessage.setTo(toReceiver.getEmail());
		simpleMailMessage.setSubject("Codigo de verificacion MyPets");
		simpleMailMessage.setText("Codigo de verificacion: " + token);
		javaMailSender.send(simpleMailMessage);
	}

}
