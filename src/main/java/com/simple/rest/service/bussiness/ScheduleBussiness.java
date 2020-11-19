package com.simple.rest.service.bussiness;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.ScheduleData;
import com.simple.rest.service.data.ShiftData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.util.Dates;

@Service
public class ScheduleBussiness {
	
	@Autowired
	ShiftBussiness shiftBussiness;
	
	@Autowired
	ScheduleData scheduleData;
	
	public static String[] DAYS = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
	
	public MyResponse create() throws SQLException, ParseException{
		
		String startDate = Dates.getDateOfFirstDayInTheWeek();
		
		String endDate = Dates.getDateOfLastDayInTheWeek();
		
		int weekNumber = Dates.getWeekNumberInYear();
		
		int datesAmountPerDay = ShiftBussiness.STARTS_HOURS.length;
		
		ArrayList<Shift[]> listShifts = new ArrayList<>();
		
		for(int i = 0; i<DAYS.length; i++) {
			
			Shift[] shifts = new Shift[datesAmountPerDay];
			
			for(int j = 0; j<datesAmountPerDay; j++) {
				
				String date = Dates.addDaysToDate(Dates.getDateForDB(startDate), i);
				
				Date dat = new SimpleDateFormat("yyyy-MM-dd").parse(date);  
				
				Shift shift = shiftBussiness.createShift(dat, j);
				
				shifts[j] = shift;
				
			}
			
			listShifts.add(shifts);
			
		}//for
		
		Schedule schedule = new Schedule();
		
		schedule.setWeekNumber(weekNumber);
		schedule.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(Dates.getDateForDB(startDate)));
		schedule.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(Dates.getDateForDB(endDate)));
		schedule.setShifts(listShifts);
		
		MyResponse mResponse = scheduleData.create(schedule);
		
		return mResponse;
		
	}
	
	

	public MyResponse get() {
		
		int weekNumber = Dates.getWeekNumberInYear();

		MyResponse mResponse = new MyResponse();
		
		try {
			mResponse = scheduleData.getSchedule(weekNumber);
			if(mResponse.getData()==null) {
				mResponse = create();//if is null, means week's schedule doesn't no exits, so will be created
				if(mResponse.isSuccessful()==false) {
					return mResponse;
				}
				mResponse = scheduleData.getSchedule(weekNumber);
			}
		} 
		catch (SQLException | ParseException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		
		return mResponse;
		
	}
	
}
