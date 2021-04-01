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
import com.simple.rest.service.util.EncryptionPasswords;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UserData userData;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		com.simple.rest.service.domain.User user = null;
		try {
			user = userData.findByEmail(userEmail);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (user.getEmail().equals(userEmail)) {
			return new User(userEmail, bCryptEncodePassword(EncryptionPasswords.bytetoString(user.getPasswordWithSalt())), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + userEmail);
		}
	}

	public UserDetails loadUserSocialLogin(String userEmail, String password) throws UsernameNotFoundException {
		return new User(userEmail, bCryptEncodePassword(password), new ArrayList<>());
	}

	private String bCryptEncodePassword(String password) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		return passwordEncoder.encode(password);

	}

}