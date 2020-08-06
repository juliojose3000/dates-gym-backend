package com.simple.rest.service.controller;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@RunWith(SpringRunner.class)
class ScheduleControllerTest {
	
	private String url = "http://localhost:8086/gymcachi/schedule";
	
	private static HttpEntity<String> request;
	
	private static RestTemplate restTemplate = new RestTemplate();

	@Test
	public void create() {
		
		ResponseEntity<Boolean> response = restTemplate.exchange(this.url+"/create", HttpMethod.GET, request, Boolean.class);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		
		boolean wasSuccessful = response.getBody();
		
		assertTrue(wasSuccessful);
		
	}

}