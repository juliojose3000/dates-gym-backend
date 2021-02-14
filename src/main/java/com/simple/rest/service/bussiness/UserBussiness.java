package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.LinkResetPassword;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Utilities;

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
	
	public boolean findUserByEmail(String email) {
		 try {
			return userData.doesUserExists(email);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public MyResponse generateLinkResetPassword(String userEmail) {
		
		MyResponse mResponse = new MyResponse();
		
		String cod = Utilities.alphaNumericRandom();
		String expireDate = Dates.getCurrentDate();
		String expireTime = Dates.getCurrentTimeInOneHour();
		boolean usedLink = false;
		
		LinkResetPassword linkResetPassword = new LinkResetPassword();
		linkResetPassword.setCode(cod);
		linkResetPassword.setUserEmail(userEmail);
		linkResetPassword.setExpireDate(expireDate);
		linkResetPassword.setExpireTime(expireTime);
		linkResetPassword.setUsedLink(usedLink);
		
		try {
			mResponse = userData.generateLinkResetPassword(linkResetPassword);
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
		}
		return mResponse;
	}

}
