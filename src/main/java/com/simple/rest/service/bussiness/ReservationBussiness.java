package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.ReservationData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;

@Service
public class ReservationBussiness {
	
	@Autowired
	ReservationData reservationData;
	
	public MyResponse make(Reservation reservation) {
		MyResponse mResponse = null;
		try {mResponse = reservationData.make(reservation);} 
		catch (SQLException e) {e.printStackTrace();}
		return mResponse;
	}
	
	public MyResponse cancel(Reservation reservation) {
		MyResponse mResponse = null;
		try {mResponse = reservationData.cancel(reservation);} 
		catch (SQLException e) {e.printStackTrace();}
		return mResponse;
	}
	

}
