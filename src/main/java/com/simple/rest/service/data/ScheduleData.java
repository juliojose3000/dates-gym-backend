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

import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Log;

@Repository
public class ScheduleData {
	
	private String tableName = "schedule";
	
	private static DataSource dataSource;
	
	private static final String TAG = "ScheduleData";
	
	@Autowired
	ShiftData shiftData;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int getLastCode() throws SQLException{
		
		int lastCode = -1;
		String query = "select max(id) as id from "+tableName+";";
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) 
				lastCode = rs.getInt("id");	
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		rs.close();
		stmt.close();
		conn.close();
		return lastCode;
		
	}
		
	public MyResponse create(Schedule schedule) throws SQLException{
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		boolean isSuccessful = true;
		MyResponse mResponse = new MyResponse();
		
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
			stmt = conn.createStatement();
			int rs = stmt.executeUpdate(query);
			
			if(rs != 0) {
				
				int recentScheduleId = getLastCode();
				
				for (Shift[] shifts : shiftsList) {
					
					for(int i = 0; i<shifts.length; i++) {
						isSuccessful = shiftData.create(shifts[i], recentScheduleId);
						if(isSuccessful==false) {
							mResponse.unexpectedErrorResponse();
							break;
						}
					}
				}
			}
			if(isSuccessful) {
				mResponse.setCode(Codes.SUCCESSFUL);
				mResponse.setDescription(Strings.SUCCESSFUL);
				mResponse.setData(schedule);
				mResponse.setSuccessful(true);
				Log.create(TAG, "Schedule was created successful = "+schedule.getStartDate() + " - "+schedule.getEndDate());
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		stmt.close();
		conn.close();
		return mResponse;
		
	}//method

	public MyResponse getSchedule(int weekNumber) throws SQLException {

		String query = "select * from schedule where week_number = "+weekNumber+";";
		MyResponse mResponse = new MyResponse();
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String startDate = rs.getString("start_date");
				String endDate = rs.getString("end_date");
				ArrayList<Shift[]> listShifts = shiftData.get(id);
				
				Schedule schedule = new Schedule();
				
				schedule.setId(id);
				schedule.setStartDate(Dates.stringToUtilDate(startDate));
				schedule.setEndDate(Dates.stringToUtilDate(endDate));
				schedule.setWeekNumber(weekNumber);
				schedule.setShifts(listShifts);
				
				mResponse.setSuccessful(true);
				mResponse.setData(schedule);
				mResponse.setTitle(Strings.SUCCESSFUL);
				mResponse.setDescription(Strings.SUCCESSFUL);
				mResponse.setCode(Codes.SUCCESSFUL);

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		rs.close();
		stmt.close();
		conn.close();
		return mResponse;
		
	}
	
	public MyResponse getCurrentSchedule() throws SQLException {

		String query = "SELECT * FROM schedule WHERE id=(SELECT max(id) FROM schedule);";
		MyResponse mResponse = new MyResponse();
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				int id = rs.getInt("id");
				String startDate = rs.getString("start_date");
				String endDate = rs.getString("end_date");
				int weekNumberYear = rs.getInt("week_number");
				ArrayList<Shift[]> listShifts = shiftData.get(id);
				
				Schedule schedule = new Schedule();
				
				schedule.setId(id);
				schedule.setStartDate(Dates.stringToUtilDate(startDate));
				schedule.setEndDate(Dates.stringToUtilDate(endDate));
				schedule.setWeekNumber(weekNumberYear);
				schedule.setShifts(listShifts);
				
				mResponse.setSuccessful(true);
				mResponse.setData(schedule);
				mResponse.setTitle(Strings.SUCCESSFUL);
				mResponse.setDescription(Strings.SUCCESSFUL);
				mResponse.setCode(Codes.SUCCESSFUL);

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		rs.close();
		stmt.close();
		conn.close();
		return mResponse;
		
	}
	
	
}
