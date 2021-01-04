package com.simple.rest.service.domain;

import java.util.Date;

import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Utilities;

public class Binnacle {
	
	private int id;
	private String action;
	private Date date;
	private String time;
	
	
	
	public Binnacle() {}

	public Binnacle(int id, String action, Date date, String time) {
		super();
		this.id = id;
		this.action = action;
		this.date = date;
		this.time = time;
	}
	
	

	public Binnacle(String action) {
		super();
		this.action = action;
		this.date = new Date();
		this.time = Utilities.getTimeWithFormat();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDate() {
		return date;
	}
	
	public Date getSQLDate() {
		return Dates.utilDateToSQLDate(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "id =" + id + ", action =" + action + ", date =" + date + ", tim e=" + time;
	}
	
	
	
	

}
