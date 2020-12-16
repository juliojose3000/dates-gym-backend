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

import com.simple.rest.service.bussiness.ServerBussiness;
import com.simple.rest.service.domain.MyResponse;


@RestController
@RequestMapping(value="/server")
@CrossOrigin(origins="*")
public class ServerController {
	
	@Autowired
	ServerBussiness serverBussiness;
	
	@RequestMapping(method = RequestMethod.GET, value="/get_time_zone")
	@ResponseBody
	public ResponseEntity<MyResponse> getTimeZone() throws SQLException, ParseException {
		
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		mResponse.setData(serverBussiness.getTimeZone());
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}

}
