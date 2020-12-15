package com.simple.rest.service.bussiness;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

		MyResponse mResponse = new MyResponse();
		
		try {
			mResponse = scheduleData.getCurrentSchedule();
			if(mResponse.getData()==null) //If is null, means week's schedule doesn't no exits, so will be created
				mResponse = create();
			else if (createNewSchedule()) //If today is Sunday at 5 pm in Costa Rica (11 pm where server is) a new schedule will be created
				mResponse = create();
			
			if(mResponse.isSuccessful()==false) 
				return mResponse;
		} 
		catch (SQLException | ParseException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		
		return mResponse;
		
	}
	
	public MyResponse timeToCreateANewSchedule() {
	
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		boolean createNewSchedule = false;
		
		Calendar c = Calendar.getInstance();
		//c.setTimeZone(TimeZone.getTimeZone("America/Costa_Rica"));

		SimpleDateFormat format = new SimpleDateFormat("E HH");
		String[] dayNameAndTime = (format.format(c.getTime())).split(" ");
		
		if(dayNameAndTime[0].equals("Tue") && Integer.parseInt(dayNameAndTime[1])>=10) {
			createNewSchedule = true;
		}
		
		mResponse.setData(dayNameAndTime[0]+" "+dayNameAndTime[1]+" create new schedule = "+createNewSchedule);
		return mResponse;
	}
	
	public boolean createNewSchedule() {
		boolean createNewSchedule = false;
	
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("E HH");
		String[] dayNameAndTime = (format.format(c.getTime())).split(" ");
		
		if(dayNameAndTime[0].equals("Sun") && Integer.parseInt(dayNameAndTime[1])>=23) 
			createNewSchedule = true;
		
		return createNewSchedule;
	}
	
}
