package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.ReservationData;
import com.simple.rest.service.domain.Reservation;

@Service
public class ReservationBussiness {
	
	@Autowired
	ReservationData reservationData;
	
	public boolean make(Reservation reservation) {
		
		boolean isSuccesful = false;
		
		try {
			isSuccesful = reservationData.make(reservation);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isSuccesful;
		
	}
	

}
