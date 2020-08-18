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

import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.util.Dates;

@SpringBootTest
@RunWith(SpringRunner.class)
class ReservationControllerTest {
	
	private String url = "http://localhost:8086/gymcachi/reservation";
	
	private HttpEntity<String> request;
	
	private RestTemplate restTemplate = new RestTemplate();

	@Test
	void make() {
		
		boolean wasSuccessfulProcess = 
				this.restTemplate.postForObject(this.url+"/make",getReservation(), Boolean.class);
		
		assertTrue(wasSuccessfulProcess == true);
		
	}
	
	public Reservation getReservation() {
		
		Reservation reservation = new Reservation();
		
		User user = new User(2, "Julio","Segura","8734-9630","juliojose3000@gmail.com", false);
		String date = "2020-08-17";
		String startHour = "9:00:00";
		
		reservation.setUser(user);
		reservation.setShiftDate(Dates.stringToUtilDate(date));
		reservation.setShiftStartHour(startHour);
		
		return reservation;
		
	}

}
