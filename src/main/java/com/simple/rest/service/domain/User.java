package com.simple.rest.service.domain;

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
