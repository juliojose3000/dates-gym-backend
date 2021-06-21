package com.simple.rest.service.util;

import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.ConfigConstants;

@Repository
public class EmailHtmlBodies {
	
	public String getFrontEndBaseURL() {
		if (ConfigConstants.PRODUCTION)//If the app is in production, so use the firebase app url address
			return ConfigConstants.FRONTEND_URL_PRODUCTION;
		else
			return ConfigConstants.FRONTEND_URL_DEVELOP;//In the other hand, use the local url address
	}
	
	public String getBackEndBaseURL() {
		if (ConfigConstants.PRODUCTION)//If the app is in production, so use the firebase app url address
			return ConfigConstants.BACKEND_URL_PRODUCTION;
		else
			return ConfigConstants.BACKEND_URL_DEVELOP;//In the other hand, use the local url address
	}

	
	public String generateResetPasswordLinkEmailBody(String username, String codeReset) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "	<div style=\"width: 40%;overflow: hidden; background-color: rgb(27, 24, 24); left: 0; right: 0; margin: auto; text-align: center; display: inline-block; min-width: 300px; padding: 30px;position: absolute; border-radius: 20px;\">\r\n"
				+ "\r\n"
				+ "	 	<img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\" alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\"> \r\n"
				+ "		<h1 style=\"color: white;\">Cachí Fitness Center</h1>\r\n"
				+ "		<h3 style=\"color: white; \">Has solicitado resetear tu contraseña</h3>\r\n"
				+ "		<p style=\"color: white;\">Hola "+username+", para poder restablecer tu contraseña, has click en el botón de abajo</p>\r\n"
				+ "\r\n"
				+ "		<a href='"+getFrontEndBaseURL()+"/reset_password?code="+codeReset+"' style=\"text-decoration: none; border-radius: 20px; border: 1px solid #FF4B2B; background-color: #FF4B2B; color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 12px 30px;letter-spacing: 1px; text-transform: uppercase; transition: transform 80ms ease-in; display: block; margin: 0 auto; margin-top: 35px; cursor: pointer; width: 200px;\">Resetear mi contraseña</a>\r\n"
				+ "\r\n"
				+ "	</div>\r\n"
				+ "\r\n"
				+ "</div>";
	}
	
	public String generateValidUserAccountEmailBody(User user) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "    <div\r\n"
				+ "        style=\"width: 40%;overflow: hidden; background-color: rgb(27, 24, 24); left: 0; right: 0; margin: auto; text-align: center; display: inline-block; min-width: 300px; padding: 30px;position: absolute; border-radius: 20px;\">\r\n"
				+ "\r\n"
				+ "        <img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\"\r\n"
				+ "            alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\">\r\n"
				+ "        <h1 style=\"color: white;\">Cachí Fitness Center</h1>\r\n"
				+ "        <h3 style=\"color: white; \">Habilitar cuenta del siguiente usuario</h3>\r\n"
				+ "        <p style=\"color: white;\">Nombre: "+user.getName()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <p style=\"color: white;\">Correo: "+user.getEmail()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <p style=\"color: white;\">Teléfono: "+user.getPhoneNumber()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <div style=\"display: flex;\">\r\n"
				+ "            <a href='"+getFrontEndBaseURL()+"/enable_user_account/"+user.getEmail()+"/"+user.getName()+"/"+user.getPhoneNumber()+"'\r\n"
				+ "                style=\"text-decoration: none; border-radius: 20px; border: 1px solid#5cd921 ; background-color:  #5cd921 ; color: #FFFFFF; font-size: 12px; font-weight: bold; padding: 12px 30px;letter-spacing: 1px; text-transform: uppercase; transition: transform 80ms ease-in; display: block; margin: 0 auto; margin-top: 35px; cursor: pointer; width: 100px;\">Habilitar</a>\r\n"
				+ "\r\n"
				+ "        </div>\r\n"
				+ "\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "</div>";
	}

}
