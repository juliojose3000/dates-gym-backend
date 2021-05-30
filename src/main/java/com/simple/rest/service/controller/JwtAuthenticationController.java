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
import com.simple.rest.service.authentication.JwtUserDetailsService;
import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.ErrorMessages;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.EncryptionPasswords;


@Controller
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService inMemoryUserDetailsService;
	
	@Autowired
	private JwtUserDetailsService jwtInMemoryUserDetailsService;
	
	@Autowired
	private UserData userData;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		
		String token = "";
		MyResponse mResponse = new MyResponse();
		
		try {
			User user = userData.findByEmail(authenticationRequest.getEmail());
			
			if(user != null) {
			
				String passwordWithSalt = EncryptionPasswords.bytetoString(EncryptionPasswords.getHashWithSalt(authenticationRequest.getPassword(), user.getSalt()));
				authenticate(authenticationRequest.getEmail(), passwordWithSalt);
				
				final UserDetails userDetails = inMemoryUserDetailsService
						.loadUserByUsername(authenticationRequest.getEmail());

				token = jwtTokenUtil.generateToken(userDetails);
				
				mResponse.setToken(token);
				mResponse.setSuccessful(true);
				mResponse.setTitle(Strings.SUCCESSFUL);
				mResponse.setDescription(Strings.LOGIN_SUCCESSFUL);
				mResponse.setCode(Codes.LOGIN_SUCCESSFUL);
				
				user.setSalt(null);
				user.setPasswordWithSalt(null);
				mResponse.setData(user);
				
			} else {
				mResponse.unexpectedErrorResponse();
				mResponse.setDescription(Strings.EMAIL_DOES_NOT_EXISTS);
				mResponse.setCode(Codes.EMAIL_DOES_NOT_EXISTS);
			}
			
		}catch(Exception e) {
			mResponse.unexpectedErrorResponse();

			if(e.getCause() != null && (e.getCause().getMessage().equals(ErrorMessages.BAD_CREDENTIALS) ||
					e.getCause().getMessage().equals(ErrorMessages.INVALID_CREDENTIALS))) {
				mResponse.setDescription(Strings.INVALID_CREDENTIALS);
				mResponse.setCode(Codes.INVALID_CREDENTIALS);
			}else {
				e.printStackTrace();
			}	
			
		}
		return ResponseEntity.ok(mResponse);
	}
	
	@RequestMapping(value = "/social-authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationTokenForSocialLogin(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		
		String token = "";
		MyResponse mResponse = new MyResponse();
		
		try {
			//authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
			
			final UserDetails userDetails = jwtInMemoryUserDetailsService
					.loadUserSocialLogin(authenticationRequest.getEmail(), authenticationRequest.getPassword());

			token = jwtTokenUtil.generateToken(userDetails);
			User user = userData.findByEmail(authenticationRequest.getEmail());
			
			mResponse.setToken(token);
			mResponse.setSuccessful(true);
			mResponse.setTitle(Strings.SUCCESSFUL);
			mResponse.setDescription(Strings.LOGIN_SUCCESSFUL);
			mResponse.setCode(Codes.LOGIN_SUCCESSFUL);
			mResponse.setData(user);
			
			
		}catch(Exception e) {
			mResponse.unexpectedErrorResponse();
			if(e.getCause().getMessage().equals(ErrorMessages.BAD_CREDENTIALS) ||
					e.getCause().getMessage().equals(ErrorMessages.INVALID_CREDENTIALS)) {
				mResponse.setDescription(Strings.INVALID_CREDENTIALS);
				mResponse.setCode(Codes.INVALID_CREDENTIALS);
			}else {
				e.printStackTrace();
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
