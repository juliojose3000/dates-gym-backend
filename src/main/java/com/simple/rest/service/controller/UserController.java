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

import com.simple.rest.service.authentication.JwtRequest;
import com.simple.rest.service.bussiness.UserBussiness;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Log;

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
	
	
	@RequestMapping(method = RequestMethod.POST, value="/social-login")
	@ResponseBody
	public ResponseEntity<MyResponse> socialLogin(@RequestBody User user) throws SQLException, ParseException {
		Log.create(this.getClass().getName(), "Utilizando el Login por medio de red social... ");
		
		boolean isUserRegistered = userBussiness.findUserByEmail(user.getEmail());
		
		ResponseEntity<?> response;
		
		MyResponse mResponse = new MyResponse();
		
		if(isUserRegistered) {
			try {
				Log.create(this.getClass().getName(), "El usuario ya existe, iniciando sesión");
				response = jwtAuth.createAuthenticationTokenForSocialLogin(new JwtRequest(user.getEmail(), user.getPassword()));
				MyResponse mResponseLogin = (MyResponse) response.getBody();
				mResponse.setSuccessful(mResponseLogin.isSuccessful());
				mResponse.setToken(mResponseLogin.getToken());
				mResponse.setData(mResponseLogin.getData());
				mResponse.setTitle(Strings.SUCCESSFUL);
				mResponse.setDescription(Strings.LOGIN_SUCCESSFUL);
			} catch (Exception e) {
				e.printStackTrace();
				mResponse.unexpectedErrorResponse();
			}
		}else {
			Log.create(this.getClass().getName(), "El usuario NO existe, registrándolo");
			return create(user);

		}
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reset-password")
	@ResponseBody
	public ResponseEntity<MyResponse> sendLinkResetPassword(@RequestParam String email) throws SQLException, ParseException {
		
		MyResponse mResponse = new MyResponse();
		
		if(!userBussiness.findUserByEmail(email)) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.EMAIL_DOES_NOT_EXISTS);
			mResponse.setCode(Codes.EMAIL_DOES_NOT_EXISTS);
		}else {
			
			mResponse = userBussiness.generateLinkResetPassword(email);
			
		}

		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	

}
