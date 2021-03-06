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
import com.simple.rest.service.domain.Schedule;

@RestController
@RequestMapping(value="/schedule")
@CrossOrigin(origins="*")
public class ScheduleController {
	
	@Autowired
	ScheduleBussiness scheduleBussiness;
	
	@RequestMapping(method = RequestMethod.GET, value="/create")
	@ResponseBody
	public ResponseEntity<Boolean> create() throws SQLException, ParseException {
		
		boolean isSuccessful = scheduleBussiness.create();

		return new ResponseEntity<Boolean>(isSuccessful, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/get")
	@ResponseBody
	public ResponseEntity<Schedule> get() throws SQLException, ParseException {
		
		Schedule schedule = scheduleBussiness.get();

		return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);
		
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value="/reserve/{id}")
	@ResponseBody
	public ResponseEntity<Schedule> reserveDate(@RequestParam Date date, @RequestParam String startHour) {
		
		Schedule schedule = null;
		
		try {
			schedule = scheduleBussiness.create();
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Schedule>(schedule, HttpStatus.OK);
		
	}*/

}
