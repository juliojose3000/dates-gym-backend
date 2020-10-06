package com.simple.rest.service.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.json.JSONObject;
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
import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Token;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ScheduleControllerTest {
	
	private String url = "http://localhost:8086/gymcachi/schedule";
	
	private static HttpEntity<String> request;
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static String username = "juliojose3000@gmail.com";
	
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
		
		Authentication auth = new Authentication(username, password);
		
		Token token = 
				
			restTemplate.postForObject("http://localhost:8086/gymcachi/authenticate", auth, Token.class);
		
		System.out.println(token.getToken());
		
		return token.toString();
		
	}

	//@Test
	public void create() {
		
		ResponseEntity<Boolean> response = restTemplate.exchange(this.url+"/create", HttpMethod.GET, request, Boolean.class);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		boolean wasSuccessful = response.getBody();
		assertTrue(wasSuccessful);
		
	}
	
	@Test
	public void get() {
		
		ResponseEntity<Schedule> response = restTemplate.exchange(this.url+"/get", HttpMethod.GET, request, Schedule.class);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		Schedule schedule = response.getBody();
		assertTrue(schedule.getShifts().size()>0);
		
	}
	

}