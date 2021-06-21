package com.simple.rest.service.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashMap;

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
import com.simple.rest.service.domain.LinkResetPassword;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.ResetPassword;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
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
	
	@RequestMapping(method = RequestMethod.GET, value="/send-link-reset-password")
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
	
	@RequestMapping(method = RequestMethod.POST, value="/reset-password")
	@ResponseBody
	public ResponseEntity<MyResponse> resetPassword(@RequestBody ResetPassword resetPassword) throws SQLException, ParseException {
		
		MyResponse mResponse = new MyResponse();
		LinkResetPassword linkResetPassword = userBussiness.getResetLinkByCode(resetPassword.getResetLinkCode());
		
		if(!userBussiness.findUserByEmail(resetPassword.getUserEmail())) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.EMAIL_DOES_NOT_EXISTS);
			mResponse.setCode(Codes.EMAIL_DOES_NOT_EXISTS);
		}else if(!Dates.currentTimeIsPreviusTo(linkResetPassword.getExpireDate()+" "+linkResetPassword.getExpireTime())){
			mResponse.errorResponse();
			mResponse.setDescription(Strings.LINK_RESET_HAS_EXPIRED);
			mResponse.setCode(Codes.LINK_RESET_HAS_EXPIRED);
		}else if(userBussiness.resetLinkHasBeenUsed(linkResetPassword.getCode(), linkResetPassword.getExpireDate(), linkResetPassword.getExpireTime())){
			mResponse.errorResponse();
			mResponse.setDescription(Strings.RESET_LINK_HAS_BEEN_USED);
			mResponse.setCode(Codes.RESET_LINK_HAS_BEEN_USED);
		}else if(!resetPassword.getUserEmail().equals(linkResetPassword.getUserEmail())){
			mResponse.errorResponse();
			mResponse.setDescription(Strings.THE_EMAIL_NOT_CORRESPONDS_WITH_THE_LINK_PASSWORD_RESET);
		}else {
			mResponse = userBussiness.updatePassword(resetPassword);
		}
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/update_user_profile")
	@ResponseBody
	public ResponseEntity<MyResponse> updateUserProfile(@RequestBody LinkedHashMap<String, Object> map) throws SQLException, ParseException {
		
		User user = new User((LinkedHashMap<String, String>)map.get("user"));
		
		String newPassword = String.valueOf(map.get("password"));
		
		MyResponse mResponse = userBussiness.updateUserProfile(user, newPassword);
		
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/enable_user_account")
	@ResponseBody
	public ResponseEntity<MyResponse> enableUserAccount(@RequestParam String userEmail) throws SQLException, ParseException {
		
		MyResponse mResponse = userBussiness.enableUserAccount(userEmail);
		
		return new ResponseEntity<MyResponse>(mResponse, HttpStatus.OK);
		
	}
	

}
