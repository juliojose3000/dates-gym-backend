package com.simple.rest.service.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
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
	
	public boolean make(Reservation reservation) throws SQLException {
		
		Connection  conn = dataSource.getConnection();
		
		boolean wasSuccessfulProcess = false;
	
		User user = reservation.getUser();
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
			
			if(rs != 0) {wasSuccessfulProcess = true;}
			
			stmt.close();
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return wasSuccessfulProcess;
		
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
