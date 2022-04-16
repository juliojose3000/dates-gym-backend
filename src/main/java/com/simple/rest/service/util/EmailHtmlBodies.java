package com.simple.rest.service.util;

import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.ConfigConstants;

@Repository
public class EmailHtmlBodies {
	
	public String generateResetPasswordLinkEmailBody(String username, String resetCode) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "    <div\r\n"
				+ "        style=\"width: 40%;overflow: hidden; background-color: rgb(27, 24, 24); left: 0; right: 0; margin: auto; text-align: center; display: inline-block; min-width: 300px; padding: 30px;position: absolute; border-radius: 20px;\">\r\n"
				+ "\r\n"
				+ "        <img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\"\r\n"
				+ "            alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\">\r\n"
				+ "        <h1 style=\"color: white;\">Cachí Fitness Center</h1>\r\n"
				+ "        <h3 style=\"color: white; \">Has solicitado resetear tu contraseña</h3>\r\n"
				+ "        <p style=\"color: white;\">Hola "+username+", para poder restablecer tu contraseña, has click en el botón de abajo\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <a href='"+ConfigConstants.FRONTEND_URL+"/reset_password?code="+resetCode+"'\r\n"
				+ "            style=\"border-radius: 20px;color: #f4511e;font-size: 12px;font-weight: bold;padding: 12px 30px;letter-spacing: 1px;text-transform: uppercase;transition: transform 80ms ease-in;display: block;margin: 0 auto;margin-top: 15px;cursor: pointer; border-color: #f4511e; border-style: solid; text-decoration: none;\">Resetear mi contraseña</a>\r\n"
				+ "\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "</div>";
	}
	
	public String generateValidateUserAccountEmailBody(User user) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "    <div\r\n"
				+ "        style=\"width: 40%;overflow: hidden; background-color: rgb(27, 24, 24); left: 0; right: 0; margin: auto; text-align: center; display: inline-block; min-width: 300px; padding: 30px;position: absolute; border-radius: 20px;\">\r\n"
				+ "\r\n"
				+ "        <img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\"\r\n"
				+ "            alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\">\r\n"
				+ "        <h1 style=\"color: white;\">Cachí Fitness Center</h1>\r\n"
				+ "        <h3 style=\"color: white; \">Un nuevo cliente se ha registrado</h3>\r\n"
				+ "        <p style=\"color: white;\">Nombre: "+user.getName()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <p style=\"color: white;\">Correo: "+user.getEmail()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <p style=\"color: white;\">Teléfono: "+user.getPhoneNumber()+"\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <a href='"+ConfigConstants.FRONTEND_URL+"/admin/enable_user_account/"+user.getEmail()+"/"+user.getName()+"/"+user.getPhoneNumber()+"'\r\n"
				+ "            style=\"border-radius: 20px;color: #f4511e;font-size: 12px;font-weight: bold;padding: 12px 30px;letter-spacing: 1px;text-transform: uppercase;transition: transform 80ms ease-in;display: block;margin: 0 auto;margin-top: 15px;cursor: pointer; border-color: #f4511e; border-style: solid; text-decoration: none;\">Habilitar cuenta</a>\r\n"
				+ "\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "</div>";
	}
	
	public String generateYourAccountHasBeenEnabledEmailBody(String userName) {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "    <div style=\"width: 40%;overflow: hidden;background-color: #1b2029;left: 0;right: 0;margin: auto;text-align: center;display: inline-block;min-width: 300px;padding: 30px;position: absolute;border-radius: 20px;align-items: center;\">\r\n"
				+ "\r\n"
				+ "        <img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\" alt=\"Cachí Fitness Center Logo\" width=\"100\" height=\"100\">\r\n"
				+ "        <h1 style=\"color: white;\">Cachi Fitness Center</h1>\r\n"
				+ "        <h3 style=\"color: white; \">Tu cuenta ya ha sido habilitada</h3>\r\n"
				+ "        <p style=\"color: white;\">Hola "+userName+", tu cuenta ya se encuentra habilitada, a partir de ahora podrás reservar los espacios que necesites para utilizar las instalaciones del Gimnacio Cachi Fitness Center.\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "        <a href=\""+ConfigConstants.FRONTEND_URL+"/gym_services/weight_room\" style=\"border-radius: 20px;color: #f4511e;font-size: 12px;font-weight: bold;padding: 12px 30px;letter-spacing: 1px;text-transform: uppercase;transition: transform 80ms ease-in;display: block;margin: 0 auto;margin-top: 15px;cursor: pointer; border-color: #f4511e; border-style: solid; text-decoration: none;\">Reservar espacios</a>\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "</div>";
	}
	
	
	public static String generateTestEmailBody() {
		return "<div style=\"width: 100%;height: 100%;\">\r\n"
				+ "\r\n"
				+ "    <div style=\"width: 40%;max-width: 400px;overflow: hidden;background-color: #1b2029;left: 0;right: 0;margin: auto;text-align: center;display: inline-block;min-width: 300px;padding: 30px;position: absolute;border-radius: 20px;align-items: center;\">\r\n"
				+ "\r\n"
				+ "        <img src=\"https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/Images/body-icon.png\" alt=\"CachÃ­ Fitness Center Logo\" width=\"100\" height=\"100\">\r\n"
				+ "        <h1 style=\"color: white;\">Cachi Fitness Center</h1>\r\n"
				+ "\r\n"
				+ "        <p style=\"color: white;\">Este correo es de prueba. Saludos a todos.\r\n"
				+ "        </p>\r\n"
				+ "\r\n"
				+ "        <a href=\"#\" style=\"border-radius: 20px;color: #f4511e;font-size: 12px;font-weight: bold;padding: 12px 30px;letter-spacing: 1px;text-transform: uppercase;transition: transform 80ms ease-in;display: block;margin: 0 auto;margin-top: 15px;cursor: pointer;border-color: #f4511e; border-style: solid; text-decoration: none;\" data-linkindex=\"0\">Prueba Botón</a>\r\n"
				+ "\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "</div>";
	}

	
	
}
