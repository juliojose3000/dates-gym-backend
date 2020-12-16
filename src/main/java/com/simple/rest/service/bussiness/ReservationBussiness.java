package com.simple.rest.service.bussiness;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.ReservationData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.resources.Strings;

@Service
public class ReservationBussiness {
	
	@Autowired
	ReservationData reservationData;
	
	public MyResponse make(Reservation reservation) {
		
		MyResponse mResponse = new MyResponse();
		
		if(!isAValisReservation(reservation)) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.INVALID_RESERVATION);
			return mResponse;
		}
		
		try {
			mResponse = reservationData.make(reservation);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}
	
	public MyResponse cancel(Reservation reservation) {
		
		MyResponse mResponse = new MyResponse();
		
		if(!isAValisReservation(reservation)) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.INVALID_RESERVATION);
			return mResponse;
		}
		
		try {
			mResponse = reservationData.cancel(reservation);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		return mResponse;
	}
	
	/* This method checks if the date and time of reservation are after from now */
	private boolean isAValisReservation(Reservation reservation) {
		
		ZoneId z = ZoneId.systemDefault() ;
		String myTimeZone = z.toString();
		
		Date date = reservation.getShiftDate();
		String[] time = reservation.getShiftStartHour().split(":");
		
		if(myTimeZone.equals("GMT-06:00"))
			date.setHours(Integer.parseInt(time[0]));

		Date thisMomentDate = new Date();
		
		if(thisMomentDate.after(date)) {
			return false;
		}
		
		return true;

	}

}
