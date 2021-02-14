package com.simple.rest.service.domain;

public class LinkResetPassword {
	
	private String code;
	private String userEmail;
	private String expireTime;
	private String expireDate;
	private boolean usedLink;
	
	public LinkResetPassword() {}
	
	public LinkResetPassword(String code, String userEmail, String expireTime, String expireDate, boolean usedLink) {
		this.code = code;
		this.userEmail = userEmail;
		this.expireTime = expireTime;
		this.expireDate = expireDate;
		this.usedLink = usedLink;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isUsedLink() {
		return usedLink;
	}

	public void setUsedLink(boolean usedLink) {
		this.usedLink = usedLink;
	}
	
}
