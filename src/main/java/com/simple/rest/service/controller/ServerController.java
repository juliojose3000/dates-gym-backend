package com.simple.rest.service.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.rest.service.bussiness.ServerBussiness;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.resources.TimeZoneStrings;


@RestController
@RequestMapping(value="/server")
@CrossOrigin(origins="*")
public class ServerController {
	
	@Autowired
	ServerBussiness serverBussiness;
	
	@RequestMapping(method = RequestMethod.GET, value="/get_time_zone")
	@ResponseBody
	public ResponseEntity<MyResponse> getServerTimeZone() throws SQLException, ParseException {
		
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		mResponse.setData(serverBussiness.getServerTimeZone());
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get_current_time")
	@ResponseBody
	public ResponseEntity<MyResponse> getCurrentTimeServer() throws SQLException, ParseException {
		
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		mResponse.setData(serverBussiness.getServerCurrentTime());
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/reservation_details")
	@ResponseBody
	private MyResponse reservationServerDetails(@RequestBody Reservation reservation) {
		return serverBussiness.reservationServerDetails(reservation);
	}

}
