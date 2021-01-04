package com.simple.rest.service.bussiness;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.resources.TimeZoneStrings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Utilities;

@Service
public class ServerBussiness {
	
	public String getServerTimeZone() {
		return Utilities.getTimeZoneServer();
	}
	
	public String getServerCurrentTime() {
		return Utilities.getCurrentTimeServer();
	}
	
	public MyResponse reservationServerDetails(Reservation reservation) {
		
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		
		String data = "Reservation date: "+reservation.getShiftDate()+" | time: "+reservation.getShiftStartHour();
		
		ZoneId z = ZoneId.systemDefault() ;
		String myTimeZone = z.toString();
		
		Date date = reservation.getShiftDate();
		String[] time = reservation.getShiftStartHour().split(":");
		
		if(myTimeZone.equals(TimeZoneStrings.COSTA_RICA)) {
			date.setDate(date.getDate()+1);//For a strange reason, the reservation date arrives with a day less, so it is added
			date.setHours(Integer.parseInt(time[0]));
		}
		else if(myTimeZone.equals(TimeZoneStrings.AZURE_SERVER_WEST_US))
			date.setHours(Integer.parseInt(time[0])+6);
		
		data += "| time for server: "+date.toString();

		Date thisMomentDate = new Date();
		
		if(thisMomentDate.after(date)) {
			data+="| Is a valid reservation: "+false;
		}else {
			data+="| Is a valid reservation: "+true;
		}
		
		mResponse.setData(data);
		
		return mResponse;

	}
	
	public int getCurrentWeekNumber() {
		return Dates.getWeekNumberInYear();
	}
	

}
