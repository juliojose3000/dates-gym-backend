package com.simple.rest.service.domain;

import java.io.Serializable;

import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;

public class MyResponse implements Serializable {
	
	private static final long serialVersionUID = -8091879091924046844L;
	private boolean isSuccessful;
	private String title;
	private String description;
	private int code;
	private String token;
	private Object data;
	
	
	
	public MyResponse() {
		super();
	}


	public MyResponse(boolean isSuccessful, String title, String message, int code, String token, Object data) {
		super();
		this.isSuccessful = isSuccessful;
		this.title = title;
		this.description = message;
		this.code = code;
		this.token = token;
		this.data = data;
	}

	@JsonProperty("isSuccessful")
	public boolean isSuccessful() {
		return isSuccessful;
	}


	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String message) {
		this.description = message;
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

	public void unexpectedErrorResponse() {
		this.title = Strings.ERROR;
		this.code = Codes.UNEXPECTED_ERROR;
		this.description = Strings.UNEXPECTED_ERROR;
		this.isSuccessful = false;
		this.data = null;
		this.token = null;
	}
	
	public void successfulResponse() {
		this.title = Strings.SUCCESSFUL;
		this.code = Codes.SUCCESSFUL;
		this.description = Strings.SUCCESSFUL;
		this.isSuccessful = true;
		this.data = null;
		this.token = null;
	}
	
	public JSONObject toJson() {
		JSONObject mResponseToJson = new JSONObject();
		mResponseToJson.put("title", this.title);
		mResponseToJson.put("code", this.code);
		mResponseToJson.put("description", this.description);
		mResponseToJson.put("isSuccessful", this.isSuccessful);
		mResponseToJson.put("data", true);
		mResponseToJson.put("token", this.token);
		mResponseToJson.put("data", this.data);
		return mResponseToJson;
	}
	

}
