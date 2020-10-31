package com.simple.rest.service.domain;

public class MyResponse {
	
	private boolean isSuccessful;
	private String message;
	private int code;
	private String token;
	private Object data;
	
	
	
	public MyResponse() {
		super();
		this.isSuccessful = false;
	}


	public MyResponse(boolean isSuccessful, String message, int code, String token, Object data) {
		super();
		this.isSuccessful = isSuccessful;
		this.message = message;
		this.code = code;
		this.token = token;
		this.data = data;
	}


	public boolean isSuccessful() {
		return isSuccessful;
	}


	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public String getToken() {
		return "Bearer "+ token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
	

}
