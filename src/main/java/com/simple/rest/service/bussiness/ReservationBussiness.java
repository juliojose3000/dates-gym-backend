package com.simple.rest.service.bussiness;

import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.ReservationData;
import com.simple.rest.service.data.UserData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.resources.TimeZoneStrings;
import com.simple.rest.service.util.Log;

@Service
public class ReservationBussiness {
	
	@Autowired
	ReservationData reservationData;
	
	@Autowired
	UserData userData;
	
	private static final String TAG = "ReservationBussiness";
	
	public MyResponse make(Reservation reservation) {
		
		MyResponse mResponse = new MyResponse();
		
		if(!userData.userIsEnabled(reservation.getUser().getEmail())) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.USER_ACCOUNT_IS_NOT_ENABLED);
			return mResponse;
		}else if(!isAValisReservation(reservation)) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.INVALID_RESERVATION);
			return mResponse;
		}else {
			try {
				mResponse = reservationData.make(reservation);
			} 
			catch (SQLException | InterruptedException e) {
				e.printStackTrace();
				Log.create(TAG, e.getMessage());
				mResponse.unexpectedErrorResponse();
			}
			return mResponse;
		}
		
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
            Log.error(TAG, e.getMessage());
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
		
		if(myTimeZone.equals(TimeZoneStrings.COSTA_RICA)) {
			date.setDate(date.getDate()+1); //For a strange reason, the reservation date arrives with a day less, so it is added
			date.setHours(Integer.parseInt(time[0]));
		}
		else if(myTimeZone.equals(TimeZoneStrings.AZURE_SERVER_WEST_US))
			date.setHours(Integer.parseInt(time[0])+6);

		Date thisMomentDate = new Date();
		
		if(thisMomentDate.after(date)) 
			return false;
		
		
		return true;

	}

}
