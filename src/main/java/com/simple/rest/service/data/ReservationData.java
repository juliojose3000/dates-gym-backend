package com.simple.rest.service.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;

@Repository
public class ReservationData {
	
	private static DataSource dataSource;
	
	private String tableName = "reservation";
	
	@Autowired
	UserData userData;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public MyResponse make(Reservation reservation) throws SQLException {
		
		Connection  conn = dataSource.getConnection();
		MyResponse mResponse = new MyResponse();
		
		User user = userData.findByEmail(reservation.getUser().getEmail());
		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();

		String query = "insert into reservation(\r\n" + 
				"id_user, date_shift, start_hour_shift) \r\n" + 
				"values ("
				+ "'"+user.getId()+"',"
				+ "'"+Dates.utilDateToString(shiftDate)+"',"
				+ "'"+shiftStartHour+"');";

		try {
			
			Statement stmt = conn.createStatement();
			System.out.println(query);
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.RESERVATION_SUCCESSFUL);
				mResponse.setMessage(Strings.RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);
				
				String callSP = "{call update_available_space('"+Dates.utilDateToString(shiftDate)+"', '"+shiftStartHour+"')}"; 
				CallableStatement statement = conn.prepareCall(callSP);  
				statement.execute(); 
			}
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {
			
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);
			
			switch(e.getErrorCode()) {
				case Codes.DUPLICATE_ENTRY_ERROR:
					mResponse.setMessage(Strings.DUPLICATE_ENTRY_ERROR);
					break;
				case Codes.NO_AVAILABLE_SPACE:
					mResponse.setMessage(Strings.NO_AVAILABLE_SPACE);
					break;
				default:
					e.printStackTrace();
					mResponse.unexpectedErrorResponse();
					break;
			}

		}
		
		return mResponse;
		
	}
	
	public MyResponse cancel(Reservation reservation) throws SQLException {
		
		Connection  conn = dataSource.getConnection();
		
		MyResponse mResponse = new MyResponse();
	
		User user = userData.findByEmail(reservation.getUser().getEmail());
		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();

		String query = "delete from reservation where id_user = "+user.getId()+
				" AND date_shift = '"+Dates.utilDateToString(shiftDate)+"';";

		try {
			
			Statement stmt = conn.createStatement();
			System.out.println(query);
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setMessage(Strings.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);
				
				String callSP = "{call cancel_reservation("+user.getId()+", '"+Dates.utilDateToString(shiftDate)+"', '"+shiftStartHour+"')}"; 
				CallableStatement statement = conn.prepareCall(callSP);  
				statement.execute(); 
			}
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		
		return mResponse;
		
	}
	
	public ArrayList<User> getClients(String date, String startHour) throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		String query = "select id_user from reservation where date_shift = '"+date+"' and start_hour_shift = '"+startHour+"';";
		ArrayList<User> listUsers = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				int id = rs.getInt("id_user");
				User user = userData.findById(id);
				listUsers.add(user);
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listUsers;
		
	}
	

}
