package com.simple.rest.service.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.simple.rest.service.domain.Authentication;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Token;
import com.simple.rest.service.domain.User;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserControllerTest {
	
	private String url = "http://localhost:8080/gymcachi/user";
	
	private static HttpEntity<String> request;
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static String email = "juliojose3000@gmail.com";
	
	private static String password = "123";
	
	private static String token;
	
	private static HttpHeaders headers;
	
	@BeforeAll
	public static void init() {
		
		token = getToken();
		
		headers = new HttpHeaders();
		
		headers.add("Authorization", token);
		
		request = new HttpEntity<String>(headers);
		
	}
	
	public static String getToken() {
		
		Authentication auth = new Authentication(email, password);
		
		Token token = 
				
			restTemplate.postForObject("http://localhost:8086/gymcachi/authenticate", auth, Token.class);
		
		System.out.println(token.getToken());
		
		return token.toString();
		
	}
	
	@Test
	void register() {
		Boolean isSuccessful = 
				restTemplate.postForObject(this.url+"/create", getUser(), Boolean.class);
		
		assertTrue(isSuccessful);
	}
	
	public User getUser() {
		
		return new User(1, "Charlie Alvarado","87310475","charlie97@gmail.com","123");
		
	}
	
	

}
