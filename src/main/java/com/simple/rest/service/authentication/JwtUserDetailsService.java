package com.simple.rest.service.authentication;

import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.UserData;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserData userData;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		com.simple.rest.service.domain.User user = null;
		try {user = userData.findByUsername(username);} 
		catch (SQLException e) {e.printStackTrace();}
		
		if (user.getUsername().equals(username)) {
			return new User(username, bCryptEncodePassword(user.getPassword()), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	
	private String bCryptEncodePassword(String password) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		return passwordEncoder.encode(password);
		
	}

}