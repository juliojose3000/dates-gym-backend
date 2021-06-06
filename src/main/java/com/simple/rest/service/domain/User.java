package com.simple.rest.service.domain;

import java.util.LinkedHashMap;

import com.simple.rest.service.data.UserData;
import com.simple.rest.service.util.Utilities;

public class User {
	
	private int id;
	private String name;
	private String phoneNumber;
	private String email;
	private String password;
	private byte[] salt;
	private byte[] passwordWithSalt;
	
	public User() {}

	public User(int id, String name, String phoneNumber, String email, String password, byte[] salt, byte[] passwordWithSalt) {
		super();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.passwordWithSalt = passwordWithSalt;
	}
	
	public User(User user) {
		super();
		this.id = user.getId();
		this.name = user.getName();
		this.phoneNumber = user.getPhoneNumber();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.salt = user.getSalt();
		this.passwordWithSalt = user.getPasswordWithSalt();
	}
	
	public User(LinkedHashMap<String, String> userMap) {
		super();
		this.id = Integer.parseInt(userMap.get("id"));
		this.name = userMap.get("name");
		this.phoneNumber = userMap.get("phoneNumber");
		this.email = userMap.get("email");
		this.password = userMap.get("password");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public byte[] getPasswordWithSalt() {
		return passwordWithSalt;
	}

	public void setPasswordWithSalt(byte[] passwordWithSalt) {
		this.passwordWithSalt = passwordWithSalt;
	}

	
	
	
	


}
