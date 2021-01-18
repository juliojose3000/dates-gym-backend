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

import com.simple.rest.service.domain.Binnacle;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;

@Repository
public class ReservationData {
	
	public static DataSource dataSource;
	
	public static Connection  conn;
	
	private String tableName = "reservation";
	
	@Autowired
	UserData userData;
	
	private boolean CONN_IS_NOT_CLOSED = false;
	
	@Autowired 
	BinnacleData binnacleData;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	
	public MyResponse make(Reservation reservation) throws SQLException {
		
		boolean aux = true;
		while(conn!=null && conn.isClosed()==CONN_IS_NOT_CLOSED) {
			if(aux) {
				System.out.println("Otra sesión está usando la conexión, esperando a que finalice...");
				aux = false;
			}
		}
		System.out.println("Procediendo con la reservación...");
		
		conn = dataSource.getConnection();
		Statement stmt = null;
		
		MyResponse mResponse = new MyResponse();
		
		User user = userData.findByEmail(reservation.getUser().getEmail());
		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();
		

		if(!thereIsAvailableSpace(shiftDate, shiftStartHour)) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.NO_AVAILABLE_SPACE);
			return mResponse;
		}
		
		String query = "insert into reservation(id_user, date_shift, start_hour_shift) values ("
				+ "'"+user.getId()+"','"+Dates.utilDateToString(shiftDate)+"','"+shiftStartHour+"');";

		try {
			stmt = conn.createStatement();
			System.out.println(query);
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.RESERVATION_SUCCESSFUL);
				mResponse.setDescription(Strings.RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);
				
				String callSP = "{call update_available_space('"+Dates.utilDateToString(shiftDate)+"', '"+shiftStartHour+"')}"; 
				CallableStatement statement = conn.prepareCall(callSP);  
				statement.execute(); 
				
				binnacleData.addRecord(new Binnacle("Make reservation by "+user.getId()+" on "+Dates.utilDateToString(shiftDate) + " at "+shiftStartHour));
			}
			
		} catch (SQLException e) {
			
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);
			
			switch(e.getErrorCode()) {
				case Codes.DUPLICATE_ENTRY_ERROR:
					mResponse.setDescription(Strings.DUPLICATE_ENTRY_RESERVATION_ERROR);
					break;
				case Codes.NO_AVAILABLE_SPACE:
					mResponse.setDescription(Strings.NO_AVAILABLE_SPACE);
					break;
				default:
					e.printStackTrace();
					mResponse.unexpectedErrorResponse();
					break;
			}

		}
		stmt.close();
		conn.close();
		return mResponse;
		
	}
	
	public MyResponse cancel(Reservation reservation) throws SQLException {
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		
		MyResponse mResponse = new MyResponse();
	
		User user = userData.findByEmail(reservation.getUser().getEmail());
		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();

		String query = "delete from reservation where id_user = "+user.getId()+
				" AND date_shift = '"+Dates.utilDateToString(shiftDate)+"';";

		try {
			stmt = conn.createStatement();
			System.out.println(query);
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setDescription(Strings.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);
				
				String callSP = "{call cancel_reservation("+user.getId()+", '"+Dates.utilDateToString(shiftDate)+"', '"+shiftStartHour+"')}"; 
				CallableStatement statement = conn.prepareCall(callSP);  
				statement.execute(); 
				
				binnacleData.addRecord(new Binnacle("Cancel reservation by "+user.getId()+" on "+Dates.utilDateToString(shiftDate) + " at "+shiftStartHour));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mResponse.unexpectedErrorResponse();
		}
		stmt.close();
		conn.close();
		return mResponse;
		
	}
	
	public ArrayList<User> getClients(String date, String startHour) throws SQLException{
		
		String query = "select id_user from reservation where date_shift = '"+date+"' and start_hour_shift = '"+startHour+"';";
		ArrayList<User> listUsers = new ArrayList<>();
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("id_user");
				User user = userData.findById(id);
				listUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		stmt.close();
		conn.close();
		return listUsers;
		
	}
	
	public boolean thereIsAvailableSpace(Date shiftDate, String shiftStartHour) throws SQLException{
		
		String query = "SELECT available_space FROM shift WHERE shift_date="
				+ "'"+Dates.utilDateToString(shiftDate)+"' AND start_hour='"+shiftStartHour+"';";
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		int availableSpace = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				availableSpace = rs.getInt("available_space");
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		stmt.close();
		conn.close();
		return availableSpace==0?false:true;
		
	}
	

}
