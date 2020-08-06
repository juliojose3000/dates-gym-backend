package com.simple.rest.service.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.simple.rest.service.domain.User;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserControllerTest {
	
	private String url = "http://localhost:8086/gymcachi/user";
	
	private HttpEntity<String> request;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Test
	void make() {
		
		boolean wasSuccessfulProcess = 
				this.restTemplate.postForObject(this.url+"/create", getUser(), Boolean.class);
		
		assertTrue(wasSuccessfulProcess == true);
		
	}
	
	public User getUser() {
		
		return new User(2, "Julio","Segura","8734-9630","juliojose3000@gmail.com", false);
		
	}
	
	

}
