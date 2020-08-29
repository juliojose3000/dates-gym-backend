package com.simple.rest.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.simple.rest.service.authentication.WebSecurityConfig;

@SpringBootApplication
public class GymDatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymDatesApplication.class, args);
	}

}
