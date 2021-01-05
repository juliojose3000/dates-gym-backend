package com.simple.rest.service;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.simple.rest.service.authentication.WebSecurityConfig;
import com.simple.rest.service.resources.TimeZoneStrings;

@SpringBootApplication
public class GymDatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymDatesApplication.class, args);
	}
	
    @PostConstruct
    public void init(){
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone(TimeZoneStrings.COSTA_RICA));
    }

}
