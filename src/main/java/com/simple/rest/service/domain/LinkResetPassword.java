package com.simple.rest.service.domain;

public class LinkResetPassword {
	
	private int id;
	private String code;
	private String userEmail;
	private String expireTime;
	private String expireDate;
	private boolean usedLink;
	
	public LinkResetPassword() {}
	
	public LinkResetPassword(String code, String userEmail, String expireTime, String expireDate, boolean usedLink, int id) {
		this.code = code;
		this.userEmail = userEmail;
		this.expireTime = expireTime;
		this.expireDate = expireDate;
		this.usedLink = usedLink;
		this.id = id;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int isUsedLink() {
		return usedLink==true?1:0;//In mySQL we can not save values like False or True, we only can save 0 (for False values) and Not 0 (for True values)
	}

	public void setUsedLink(int usedLink) {
		this.usedLink = usedLink==0?false:true;
	}
	
}
