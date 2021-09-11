package com.simple.rest.service.domain;

import java.util.Date;

public class Reservation {
	
	Date shiftDate;
	String shiftStartHour;
	String shiftEndHour;
	
	public Reservation() {}
	
	public Reservation(Date shiftDate, String shiftStartHour, String shiftEndHour) {
		this.shiftDate = shiftDate;
		this.shiftStartHour = shiftStartHour;
		this.shiftEndHour = shiftEndHour;
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

	public String getShiftEndHour() {
		return shiftEndHour;
	}

	public void setShiftEndHour(String shiftEndHour) {
		this.shiftEndHour = shiftEndHour;
	}
	
	
	
	

}
