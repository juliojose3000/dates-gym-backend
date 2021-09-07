package com.simple.rest.service.controller;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.rest.service.bussiness.ReservationBussiness;
import com.simple.rest.service.bussiness.ScheduleBussiness;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;

@RestController
@RequestMapping(value="/reservation")
@CrossOrigin(origins="*")
public class ReservationController {
	
	@Autowired
	ReservationBussiness reservationBussiness;
	
	@RequestMapping(method = RequestMethod.POST, value="/make")
	@ResponseBody
	public ResponseEntity<MyResponse> make(@RequestBody Reservation reservation, @RequestParam int userId) throws SQLException, ParseException {
		
		MyResponse mResponse = reservationBussiness.make(reservation, userId);

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cancel")
	@ResponseBody
	public ResponseEntity<MyResponse> cancel(@RequestBody Reservation reservation, @RequestParam int userId) throws SQLException, ParseException {
		
		MyResponse mResponse = reservationBussiness.cancel(reservation, userId);

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
}
