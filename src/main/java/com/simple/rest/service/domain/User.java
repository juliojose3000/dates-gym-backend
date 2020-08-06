package com.simple.rest.service.domain;

public class User {
	
	private int id;
	private String name;
	private String lastName;
	private String phoneNumber;
	private String email;
	private boolean isAdmin;
	
	public User() {}

	public User(int id, String name, String lastName, String phoneNumber, String email, boolean isAdmin) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.isAdmin = isAdmin;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		String s = "\n";
		return "User [" +s + 
				"id=" + id + s +
				"name=" + name + s +
				"lastName=" + lastName + s +
				"phoneNumber=" + phoneNumber + s +
				"email=" + email + s +
				"isAdmin=" + isAdmin + s +
				"]";
	}
	
	

}
