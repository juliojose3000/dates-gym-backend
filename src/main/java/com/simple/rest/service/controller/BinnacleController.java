package com.simple.rest.service.controller;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.rest.service.bussiness.BinnacleBussiness;
import com.simple.rest.service.domain.MyResponse;

@RestController
@RequestMapping(value="/binnacle")
@CrossOrigin(origins="*")
public class BinnacleController {
	
	@Autowired
	BinnacleBussiness binnacleBussiness;
	
	@RequestMapping(method = RequestMethod.GET, value="/get_all")
	@ResponseBody
	public ResponseEntity<MyResponse> get() throws SQLException, ParseException {
		
		MyResponse mResponse = binnacleBussiness.get();

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
}
