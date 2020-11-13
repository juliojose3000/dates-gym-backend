package com.simple.rest.service.domain;

import java.io.Serializable;

public class MyResponse implements Serializable {
	
	private static final long serialVersionUID = -8091879091924046844L;
	private boolean isSuccessful;
	private String message;
	private int code;
	private String token;
	private Object data;
	
	
	
	public MyResponse() {
		super();
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
		return token;
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
