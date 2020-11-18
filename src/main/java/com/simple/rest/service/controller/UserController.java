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

import com.simple.rest.service.authentication.JwtRequest;
import com.simple.rest.service.bussiness.UserBussiness;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;

@RestController
@RequestMapping(value="/user")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	UserBussiness userBussiness;
	
	@Autowired
	JwtAuthenticationController jwtAuth;
	
	@RequestMapping(method = RequestMethod.POST, value="/create")
	@ResponseBody
	public ResponseEntity<MyResponse> create(@RequestBody User user) throws SQLException, ParseException {
		
		MyResponse mResponse = userBussiness.create(user);
		ResponseEntity<?> response;
		
		if(mResponse.isSuccessful()) {
			try {
				response = jwtAuth.createAuthenticationToken(new JwtRequest(user.getEmail(), user.getPassword()));
				MyResponse mResponseLogin = (MyResponse) response.getBody();
				mResponse.setToken(mResponseLogin.getToken());
				mResponse.setData(mResponseLogin.getData());
				mResponse.setTitle(Strings.SUCCESSFUL);
				mResponse.setDescription(Strings.SIGNUP_SUCCESSFUL);
			} catch (Exception e) {
				e.printStackTrace();
				mResponse.unexpectedErrorResponse();
				mResponse.setDescription(Strings.USER_CREATED_BUT_LOGIN_FAILED);
				mResponse.setCode(Codes.USER_CREATED_BUT_LOGIN_FAILED);
			}
		}

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}

}
