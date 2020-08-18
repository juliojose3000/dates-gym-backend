package com.simple.rest.service.domain;

import java.util.Date;
import java.util.ArrayList;

public class Schedule {
	
	private int id;
	private ArrayList<Shift[]> shifts;
	private int weekNumber;
	private Date startDate;
	private Date endDate;
	
	public Schedule() {
		this.shifts = new ArrayList<Shift[]>();
	}
	
	public Schedule(int id, ArrayList<Shift[]> shifts, int weekNumber, Date startDate, Date endDate) {
		this.id = id;
		this.shifts = shifts;
		this.weekNumber = weekNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Shift[]> getShifts() {
		return shifts;
	}
	public void setShifts(ArrayList<Shift[]> shifts) {
		this.shifts = shifts;
	}
	public int getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		String s = "\n";
		return "Schedule ["+ s +
				"shifts=" + shifts + s +
				"weekNumber=" + weekNumber + s +
				"startDate=" + startDate + s +
				"endDate="+ endDate + s + "]";
	}
	
	
	
	

}
