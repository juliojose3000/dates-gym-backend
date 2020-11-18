package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;

@Service
public class UserBussiness {
	
	@Autowired
	UserData userData;
	
	public MyResponse create(User user) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.create(user);
		} catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
		
	}

}
