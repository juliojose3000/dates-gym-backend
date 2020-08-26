package com.simple.rest.service.domain;

public class User {
	
	private int id;
	private String name;
	private String lastname;
	private String phoneNumber;
	private String email;
	private String username;
	private String password;
	private boolean isAdmin;
	
	public User() {}

	public User(int id, String name, String lastName, String phoneNumber, String email, String username, String password, boolean isAdmin) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
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

	public String getLastname() {
		return lastname;
	}

	public void setLastName(String lastname) {
		this.lastname = lastname;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
				"lastname=" + lastname + s +
				"phoneNumber=" + phoneNumber + s +
				"email=" + email + s +
				"isAdmin=" + isAdmin + s +
				"]";
	}
	
	

}
