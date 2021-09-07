package com.simple.rest.service.domain;

import java.util.Date;

public class Reservation {
	
	Date shiftDate;
	String shiftStartHour;
	
	public Reservation() {}
	
	public Reservation(Date shiftDate, String shiftStartHour) {
		this.shiftDate = shiftDate;
		this.shiftStartHour = shiftStartHour;
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
