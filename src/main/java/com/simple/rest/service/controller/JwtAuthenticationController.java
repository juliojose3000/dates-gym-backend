package com.simple.rest.service.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simple.rest.service.authentication.JwtRequest;
import com.simple.rest.service.authentication.JwtTokenUtil;
import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.ErrorMessages;
import com.simple.rest.service.resources.Strings;


@Controller
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Autowired
	private UserData userData;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		
		String token = "";
		MyResponse mResponse = new MyResponse();
		
		try {
			authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
			
			final UserDetails userDetails = jwtInMemoryUserDetailsService
					.loadUserByUsername(authenticationRequest.getEmail());

			token = jwtTokenUtil.generateToken(userDetails);
			User user = userData.findByEmail(authenticationRequest.getEmail());
			
			mResponse.setToken(token);
			mResponse.setSuccessful(true);
			mResponse.setTitle(Strings.SUCCESSFUL);
			mResponse.setMessage(Strings.LOGIN_SUCCESSFUL);
			mResponse.setCode(Codes.LOGIN_SUCCESSFUL);
			mResponse.setData(user);
			
			
		}catch(Exception e) {
			
			if(e.getMessage().equals(ErrorMessages.INVALID_CREDENTIALS)) {
				mResponse.setTitle(Strings.ERROR);
				mResponse.setMessage(Strings.INVALID_CREDENTIALS);
				mResponse.setCode(Codes.INVALID_CREDENTIALS);
				mResponse.setToken(null);
				mResponse.setSuccessful(false);
				mResponse.setData(null);
			}else {
				e.printStackTrace();
				mResponse.unexpectedErrorResponse();
			}	
			
		}
		return ResponseEntity.ok(mResponse);
	}

	private void authenticate(String email, String password) throws Exception {
		Objects.requireNonNull(email);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	
}
