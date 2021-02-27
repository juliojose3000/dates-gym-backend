package com.simple.rest.service.domain;

public class ResetPassword {
	
	private String userEmail;
	private String resetLinkCode;
	private String newPassword;
	
	public ResetPassword() {}
	
	public ResetPassword(String userEmail, String resetLinkCode, String newPassword) {
		this.userEmail = userEmail;
		this.resetLinkCode = resetLinkCode;
		this.newPassword = newPassword;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getResetLinkCode() {
		return resetLinkCode;
	}
	public void setResetLinkCode(String resetLinkCode) {
		this.resetLinkCode = resetLinkCode;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	

}
