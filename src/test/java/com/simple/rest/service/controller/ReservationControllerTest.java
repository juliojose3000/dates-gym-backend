package com.simple.rest.service.controller;

import static org.junit.jupiter.api.Assertions.*;

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
import com.simple.rest.service.util.Dates;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReservationControllerTest {
	
	private String url = "http://localhost:8086/gymcachi/reservation";
	
	private static HttpEntity<String> request;
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static String username = "juliojose3000";
	
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

	@Test
	void make() {
		
		HttpEntity<Reservation> request = new HttpEntity<Reservation>(getReservation(), headers);
		ResponseEntity<Boolean> response = restTemplate.exchange(this.url+"/make", HttpMethod.POST, request, Boolean.class);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		boolean wasSuccessful = response.getBody();
		assertTrue(wasSuccessful);
		
	}
	
	public Reservation getReservation() {
		
		Reservation reservation = new Reservation();
		
		User user = new User(1, "Julio Segura","8734-9630","juliojose3000@gmail.com","");
		String date = "2020-09-20";
		String startHour = "9:00:00";
		
		reservation.setUser(user);
		reservation.setShiftDate(Dates.stringToUtilDate(date));
		reservation.setShiftStartHour(startHour);
		
		return reservation;
		
	}

}
