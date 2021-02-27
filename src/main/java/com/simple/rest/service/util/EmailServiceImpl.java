package com.simple.rest.service.util;

import java.net.UnknownHostException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Constants;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;
	
	private String url;

	public EmailServiceImpl() {
		if (emailSender == null)
			emailSender = EmailService.getJavaMailSender();
		
		if (Constants.PRODUCTION)//If the app is in production, so use the firebase app url address
			url = "https://cachi-fitness-center.web.app/";
		else
			url = "http://localhost:4200";//In the other hand, use the local url address
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("juliojose3000@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);

	}

	public void sendHTMLEmailMessage(User user, String subject, String codeReset) {
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		String htmlMsg = generateEmailBody(user.getName().split(" ")[0], codeReset);
		try {
			helper.setText(htmlMsg, true);
			helper.setTo(user.getEmail()); // Destinatario
			helper.setSubject(subject);
			helper.setFrom("juliojose3000@gmail.com"); // Emisor
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		emailSender.send(mimeMessage);
	}

	public String generateEmailBody(String username, String codeReset) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "	<div style=\"width: 40%;overflow: hidden; background-color: rgb(27, 24, 24); left: 0; right: 0; margin: auto; text-align: center; display: inline-block; min-width: 300px; padding: 30px;position: absolute; border-radius: 20px;\">\r\n"
				+ "\r\n"
				+ "	 	<img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\" alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\"> \r\n"
				+ "		<h1 style=\"color: white;\">Cachí Fitness Center</h1>\r\n"
				+ "		<h3 style=\"color: white; \">Has solicitado resetear tu contraseña</h3>\r\n"
				+ "		<p style=\"color: white;\">Hola "+username+", para poder restablecer tu contraseña, has click en el botón de abajo</p>\r\n"
				+ "\r\n"
				+ "		<a href='"+url+"/reset_password?code="+codeReset+"' style=\"text-decoration: none; border-radius: 20px; border: 1px solid #FF4B2B; background-color: #FF4B2B; color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 12px 30px;letter-spacing: 1px; text-transform: uppercase; transition: transform 80ms ease-in; display: block; margin: 0 auto; margin-top: 35px; cursor: pointer; width: 200px;\">Resetear mi contraseña</a>\r\n"
				+ "\r\n"
				+ "	</div>\r\n"
				+ "\r\n"
				+ "</div>";
	}

}
