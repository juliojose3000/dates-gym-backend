package com.simple.rest.service.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.util.Dates;

@Repository
public class ScheduleData {
	
	private String tableName = "schedule";
	
	private static DataSource dataSource;
	
	@Autowired
	ShiftData shiftData;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int getLastCode() throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		
		int lastCode = -1;
		
		String query = "select max(id) as id from "+tableName+";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {lastCode = rs.getInt("id");}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lastCode;
		
	}
		
	public boolean create(Schedule schedule) throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		
		boolean isSuccessful = false;
		
		ArrayList<Shift[]> shiftsList = schedule.getShifts();
		int weekNumber = schedule.getWeekNumber();
		java.sql.Date startDate = Dates.utilDateToSQLDate(schedule.getStartDate());
		java.sql.Date endDate = Dates.utilDateToSQLDate(schedule.getEndDate());

		String query = "insert into "+tableName+"(\r\n" + 
				"start_date, end_date, week_number) \r\n" + 
				"values ("
				+ "'"+startDate+"',"
				+ "'"+endDate+"',"
				+ ""+weekNumber+");";

		try {
			
			Statement stmt = conn.createStatement();
			
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				
				int recentScheduleId = getLastCode();
				
				for (Shift[] shifts : shiftsList) {
					
					for(int i = 0; i<shifts.length; i++) {
						
						isSuccessful = shiftData.create(shifts[i], recentScheduleId);
						
						if(isSuccessful==false) {return false;}
						
					}
					
				}
				
			}
			
			stmt.close();
			
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isSuccessful;
		
	}//method

	public Schedule getSchedule(int weekNumber) throws SQLException {

		Connection  conn = dataSource.getConnection();
		
		String query = "select * from schedule where week_number = "+weekNumber+";";

		Schedule schedule = null;
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				
				int id = rs.getInt("id");
				String startDate = rs.getString("start_date");
				String endDate = rs.getString("end_date");
				ArrayList<Shift[]> listShifts = shiftData.get(id);
				
				schedule = new Schedule();
				
				schedule.setId(id);
				schedule.setStartDate(Dates.stringToUtilDate(startDate));
				schedule.setEndDate(Dates.stringToUtilDate(endDate));
				schedule.setWeekNumber(weekNumber);
				schedule.setShifts(listShifts);

			}
			rs.close();
			stmt.close();
			conn.close();
			
		} catch (SQLException e) {e.printStackTrace();}
		
		return schedule;
		
	}
	
	
}
