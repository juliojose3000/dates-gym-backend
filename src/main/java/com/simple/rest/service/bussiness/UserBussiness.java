package com.simple.rest.service.bussiness;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.LinkResetPassword;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.ResetPassword;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.ConfigConstants;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.EmailServiceImpl;
import com.simple.rest.service.util.Utilities;

@Service
public class UserBussiness {

	@Autowired
	UserData userData;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;

	public MyResponse create(User user) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.create(user);
		} catch (SQLException | NoSuchAlgorithmException e) {
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
		
		try {
			String cod = Utilities.alphaNumericRandom();
			String expireDate = Dates.getCurrentDate();
			String expireTime = Dates.getCurrentTimeInOneHour();
			int usedLink = ConfigConstants.FALSE;
	
			LinkResetPassword linkResetPassword = new LinkResetPassword();
			linkResetPassword.setCode(cod);
			linkResetPassword.setUserEmail(userEmail);
			linkResetPassword.setExpireDate(expireDate);
			linkResetPassword.setExpireTime(expireTime);
			linkResetPassword.setUsedLink(usedLink);
	
			mResponse = userData.generateLinkResetPassword(linkResetPassword);
			if (mResponse.isSuccessful())
				sendEmail(cod, userEmail);
			
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
		}
		return mResponse;
	}

	private void sendEmail(String codeReset, String userEmail) {

		try {
			User user = userData.findByEmail(userEmail);
			emailServiceImpl.sendHTMLEmailMessage(user, Strings.EMAIL_SUBJECT, codeReset);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public LinkResetPassword getResetLinkByCode(String code) {
		
		LinkResetPassword linkResetPassword = null;

		try {
			linkResetPassword = userData.getResetPasswordLinkByCode(code);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return linkResetPassword;
		
	}

	public MyResponse updatePassword(ResetPassword resetPassword) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.resetPassword(resetPassword);
		} catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}

	public boolean resetLinkHasBeenUsed(String code, String expireDate, String expireTime) {
		try {
			return userData.resetLinkHasBeenUsed(code, expireDate, expireTime);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public MyResponse updateUserProfile(User user) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.updateUserProfile(user);
		} catch (SQLException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}

}
