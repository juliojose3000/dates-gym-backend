package com.simple.rest.service.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.rest.service.bussiness.ScheduleBussiness;
import com.simple.rest.service.data.ScheduleData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Schedule;

@RestController
@RequestMapping(value="/schedule")
@CrossOrigin(origins="*")
public class ScheduleController {
	
	@Autowired
	ScheduleBussiness scheduleBussiness;
	
	@RequestMapping(method = RequestMethod.GET, value="/create")
	@ResponseBody
	public ResponseEntity<MyResponse> create() throws SQLException, ParseException {
		
		MyResponse mResponse = scheduleBussiness.create();

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get")
	@ResponseBody
	public ResponseEntity<MyResponse> get() throws SQLException, ParseException {
		
		MyResponse mResponse = scheduleBussiness.get();

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	

}
