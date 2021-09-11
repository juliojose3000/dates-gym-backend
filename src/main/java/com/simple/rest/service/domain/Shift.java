package com.simple.rest.service.domain;

import java.util.Date;

import com.simple.rest.service.resources.ConfigConstants;
import com.simple.rest.service.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Shift {
	
	private Date date;
	private String startHour;
	private String endHour;
	private int maxSpace;
	private int reservedSpace;
	private int availableSpace;
	private ArrayList<User> clients;
	private String cssClass = "cell";
	
	private static final String TAG = "ShiftModel";
	
	public Shift() {
		this.clients = new ArrayList<User>();
		this.maxSpace = ConfigConstants.NUMBER_OF_CUSTOMERS_PER_SHIFT;
		this.reservedSpace = 0;
		this.availableSpace = getAvailableSpace();
	}
	
	public Shift(int maxSpace) {
		this.clients = new ArrayList<User>();
		this.maxSpace = maxSpace;
		this.reservedSpace = 0;
		this.availableSpace = getAvailableSpace();
	}

	public Shift(Date date, String startHour, String endHour) {
		this.date = date;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(String date) {
	    Date date1 = null;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
		}  
		this.date = date1;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public int getMaxSpace() {
		return maxSpace;
	}

	public void setMaxSpace(int maxSpace) {
		this.maxSpace = maxSpace;
	}

	public int getReservedSpace() {
		return reservedSpace;
	}

	public void setReservedSpace(int reservedSpace) {
		this.reservedSpace = reservedSpace;
	}

	public int getAvailableSpace() {
		return maxSpace-reservedSpace;
	}

	public ArrayList<User> getClients() {
		return clients;
	}

	public void setClients(ArrayList<User> clients) {
		this.clients = clients;
	}
	
	public String getCssClass() {
		return cssClass;
	}


	@Override
	public String toString() {
		String s = "\n";
		return "Shift [" + s +
				"date=" + date + s +
				"startHour=" + startHour + s +
				"endHour=" + endHour + s +
				"maxSpace=" + maxSpace + s +
				"reservedSpace=" + reservedSpace + s +
				"availableSpace=" + availableSpace + s +
				"clients=" + clients + s +
				"]";
	}
	
	

}
