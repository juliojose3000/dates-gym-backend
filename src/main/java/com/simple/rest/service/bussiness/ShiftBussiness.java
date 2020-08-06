package com.simple.rest.service.bussiness;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.simple.rest.service.domain.Shift;

@Service
public class ShiftBussiness {
	
	public static String[] STARTS_HOURS = 
		{"7:00 am","9:00 am","11:00 am","3:00 pm","5:00 pm","7:00 pm"};
	
	public static String[] ENDS_HOURS = 
		{"8:30 am","10:30 am","12:30 md","4:30 pm","6:30 pm","8:30 pm"};
	
	public Shift createShift(Date date, int i) {
		
		Shift shift = new Shift();
		
		shift.setDate(date);
		shift.setStartHour(STARTS_HOURS[i]);
		shift.setEndHour(ENDS_HOURS[i]);
		
		return shift;
		
	}

}
