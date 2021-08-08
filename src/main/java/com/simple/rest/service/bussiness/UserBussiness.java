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
import com.simple.rest.service.util.EmailHtmlBodies;
import com.simple.rest.service.util.EmailServiceImpl;
import com.simple.rest.service.util.Log;
import com.simple.rest.service.util.Utilities;

@Service
public class UserBussiness {

	@Autowired
	UserData userData;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Autowired
	EmailHtmlBodies emailHtmlBodies;
	
	private static final String TAG = "UserBussiness";

	public MyResponse create(User user) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.create(user);
			if(mResponse.isSuccessful()){
				boolean isSuccessful = true;
				if(ConfigConstants.SEND_EMAIL)
					isSuccessful = sendValidUserAccountEmailBody(user);
				if(isSuccessful) {
					Log.create("UserBussiness", "Nuevo usuario registrado. Se envió correo para proceder con la activación de la cuenta.");
				}else {
					//TODO make something when email doesn't send
				}
				
			}
		} catch (SQLException | NoSuchAlgorithmException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;

	}

	public boolean findUserByEmail(String email) {
		try {
			return userData.doesUserExists(email);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			return false;
		}
	}
	
	public User getUserByEmail(String email) {
		try {
			return userData.findByEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			return null;
		}
	}

	public MyResponse generateLinkResetPassword(String userEmail) {

		MyResponse mResponse = new MyResponse();
		
		try {
			String cod = Utilities.alphaNumericRandom(10);
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
				sendResetPasswordLinkEmailBody(cod, userEmail);
			
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		return mResponse;
	}

	private void sendResetPasswordLinkEmailBody(String codeReset, String userEmail) {

		try {
			User user = userData.findByEmail(userEmail);
			String htmlEmailBody = emailHtmlBodies.generateResetPasswordLinkEmailBody(user.getName(), codeReset);
			emailServiceImpl.sendHTMLEmailMessage(user.getEmail(), Strings.EMAIL_SUBJECT_RESET_PASSWORD_LINK, htmlEmailBody);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}

	}
	
	private boolean sendValidUserAccountEmailBody(User user) {

		String htmlEmailBody = emailHtmlBodies.generateValidateUserAccountEmailBody(user);
		boolean isSuccessful = emailServiceImpl.sendHTMLEmailMessage(user.getEmail(), Strings.EMAIL_SUBJECT_VALIDATE_USER_ACCOUNT, htmlEmailBody);
		if(isSuccessful) {
			return true;
		}else {
			return false;
		}
			
	}

	
	public LinkResetPassword getResetLinkByCode(String code) {
		
		LinkResetPassword linkResetPassword = null;

		try {
			linkResetPassword = userData.getResetPasswordLinkByCode(code);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		
		return linkResetPassword;
		
	}

	public MyResponse updatePassword(ResetPassword resetPassword) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.resetPassword(resetPassword);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}

	public boolean resetLinkHasBeenUsed(String code, String expireDate, String expireTime) {
		try {
			return userData.resetLinkHasBeenUsed(code, expireDate, expireTime);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			return false;
		}
	}

	public MyResponse updateUserProfile(User user, String newPassword) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.updateUserProfile(user, newPassword);
		} catch (SQLException | NoSuchAlgorithmException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}

	public MyResponse enableUserAccount(String userEmail) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.enableUserAccount(userEmail);
			if(mResponse.isSuccessful()) {
				String htmlEmailBody = emailHtmlBodies.generateYourAccountHasBeenEnabledEmailBody(userData.findByEmail(userEmail).getName());
				boolean isSuccessful = emailServiceImpl.sendHTMLEmailMessage(userEmail, Strings.EMAIL_SUBJECT_YOUR_ACCOUNT_HAS_BEEN_ENABLED, htmlEmailBody);
				if(!isSuccessful)
					mResponse.unexpectedErrorResponse();
			}
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}

	public MyResponse registerUserPhone(User user) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = userData.registerUserPhone(user);
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}
	
	public MyResponse userExists(String email) {
		MyResponse mResponse = new MyResponse();
		try {
			mResponse.successfulResponse();
			if(userData.findByEmail(email) == null) {
				mResponse.setSuccessful(false);
				mResponse.setCode(Codes.USER_EMAIL_DOES_NOT_EXISTS);
				mResponse.setDescription(Strings.USER_EMAIL_DOES_NOT_EXISTS);
			}else {
				mResponse.setSuccessful(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}


}
