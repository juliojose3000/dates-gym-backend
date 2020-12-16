package com.simple.rest.service.bussiness;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.simple.rest.service.domain.Shift;

@Service
public class ShiftBussiness {
	
	public static String[] STARTS_HOURS = 
		{"7:00:00","9:00:00","11:00:00","15:00:00","17:00:00","19:00:00"};
	
	public static String[] ENDS_HOURS = 
		{"8:30:00","10:30:00","12:30:00","16:30:00","18:30:00","20:30:00"};
	
	public static String[] STARTS_HOURS_WEEKEND = 
		{"7:00:00","9:00:00"};
	
	public static String[] ENDS_HOURS_WEEKEND = 
		{"8:30:00","10:30:00"};
	
	public Shift createShift(Date date, int i) {
		
		Shift shift = new Shift();
		
		shift.setDate(date);
		shift.setStartHour(STARTS_HOURS[i]);
		shift.setEndHour(ENDS_HOURS[i]);
		
		return shift;
		
	}
	
	public Shift createNullShift(Date date, int i) {
		
		Shift shift = new Shift();
		
		shift.setDate(date);
		shift.setStartHour(STARTS_HOURS[i]);
		shift.setEndHour(ENDS_HOURS[i]);
		shift.setMaxSpace(0);
		shift.setReservedSpace(0);
		
		return shift;
		
	}

}
