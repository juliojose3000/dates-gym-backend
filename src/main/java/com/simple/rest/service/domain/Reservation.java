package com.simple.rest.service.domain;

import java.util.Date;

public class Reservation {
	
	User user;
	Date shiftDate;
	String shiftStartHour;
	
	public Reservation() {}
	
	public Reservation(User user, Date shiftDate, String shiftStartHour) {
		this.user = user;
		this.shiftDate = shiftDate;
		this.shiftStartHour = shiftStartHour;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getShiftDate() {
		return shiftDate;
	}
	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}
	public String getShiftStartHour() {
		return shiftStartHour;
	}
	public void setShiftStartHour(String shiftStartHour) {
		this.shiftStartHour = shiftStartHour;
	}
	
	

}
