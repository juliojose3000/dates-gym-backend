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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.rest.service.bussiness.ScheduleBussiness;
import com.simple.rest.service.bussiness.UserBussiness;
import com.simple.rest.service.domain.User;

@RestController
@RequestMapping(value="/user")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	UserBussiness userBussiness;
	
	@RequestMapping(method = RequestMethod.POST, value="/create")
	@ResponseBody
	public ResponseEntity<Boolean> create(@RequestBody User user) throws SQLException, ParseException {
		
		boolean isSuccessful = userBussiness.create(user);

		return new ResponseEntity<Boolean>(isSuccessful, HttpStatus.OK);
		
	}

}
