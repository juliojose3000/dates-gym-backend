package com.simple.rest.service.data;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.util.Dates;


@Repository
public class ShiftData {
	
	@Autowired
	ReservationData reservationData;
	
	private static DataSource dataSource;
	
	private String tableName = "shift";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ShiftData() {}

	public boolean create(Shift shift, int scheduleId) throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		
		boolean wasSuccessfulProcess = false;
	
		java.sql.Date date = Dates.utilDateToSQLDate(shift.getDate());
		String startHour = shift.getStartHour();
		String endHour = shift.getEndHour();
		int maxSpace = shift.getMaxSpace();
		int reservedSpace = shift.getReservedSpace();
		int availableSpace = shift.getAvailableSpace();

		String query = "insert into "+tableName+"(\r\n" + 
				"shift_date, start_hour, end_hour, max_space, reserved_space, available_space, id_schedule) \r\n" + 
				"values ("
				+ "'"+date+"',"
				+ "'"+startHour+"',"
				+ "'"+endHour+"',"
				+ ""+maxSpace+","
				+ ""+reservedSpace+","
				+ ""+availableSpace+","
				+ ""+scheduleId+");";

		try {
			
			Statement stmt = conn.createStatement();
			
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {wasSuccessfulProcess = true;}
			
			stmt.close();
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return wasSuccessfulProcess;
		
	}//method
	
	
	public ArrayList<Shift> get(int scheduleId) throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		
		String query = "SELECT * FROM "+tableName;

		Shift shift;
		
		ArrayList<Shift> listShifts = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				String date = rs.getString("shift_date");
				String startHour = rs.getString("start_hour");
				String endHour = rs.getString("end_hour");
				int maxSpace = rs.getInt("max_space");
				int reservedSpace = rs.getInt("reserved_space");
				int availableSpace = rs.getInt("available_space");	
				ArrayList<User> clients = reservationData.getClients(date, startHour);

				
				shift = new Shift();
				
				shift.setDate(date);
				shift.setStartHour(startHour);
				shift.setEndHour(endHour);
				shift.setMaxSpace(maxSpace);
				shift.setReservedSpace(reservedSpace);
				shift.setClients(clients);
				
				
				listShifts.add(shift);

			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listShifts;
		
	}
	
	
}
