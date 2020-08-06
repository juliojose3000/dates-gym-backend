package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.User;

@Service
public class UserBussiness {
	
	@Autowired
	UserData userData;
	
	public boolean create(User user) {
		boolean isSuccessful = false;
		try {
			isSuccessful = userData.create(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccessful;
		
	}

}
