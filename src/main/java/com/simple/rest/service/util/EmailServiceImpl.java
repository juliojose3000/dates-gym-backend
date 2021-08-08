package com.simple.rest.service.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.simple.rest.service.resources.ConfigConstants;

@Service
public class EmailServiceImpl implements EmailService {
	
	public static final String TAG = "EmailServiceImpl";

	@Autowired
	private JavaMailSender emailSender;

	public EmailServiceImpl() {
		if (emailSender == null)
			emailSender = EmailService.getJavaMailSender();
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(ConfigConstants.SERVER_EMAIL);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);

	}

	public boolean sendHTMLEmailMessage(String userEmail, String subject, String htmlEmailBody) {
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		boolean isHtml = true;
		try {
			helper.setText(htmlEmailBody, isHtml);
			helper.setTo(userEmail); // Destinatario
			helper.setSubject(subject);
			helper.setFrom(ConfigConstants.SERVER_EMAIL); // Emisor
			emailSender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			Log.create(TAG, e.getMessage());
			return false;
		}
		
	}

}
