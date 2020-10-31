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

import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.util.Dates;

@Repository
public class ReservationData {
	
	private static DataSource dataSource;
	
	private String tableName = "reservation";
	
	private final int DUPLICATE_ENTRY_ERROR = 1062;
	private final int NO_AVAILABLE_SPACE = 4025;
	
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
			
			if(rs != 0) {mResponse.setSuccessful(true);}
			
			stmt.close();
			
			conn.close();
			
		} catch (SQLException e) {
			
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			
			switch(e.getErrorCode()) {
				case DUPLICATE_ENTRY_ERROR:
					mResponse.setMessage("No puede reservar más de un espacio el mismo día.");
					break;
				case NO_AVAILABLE_SPACE:
					mResponse.setMessage("No quedan espacios disponibles.");
					break;
				default:
					System.err.print(e.getMessage());
					System.err.print("Error code: "+e.getErrorCode());
					break;
			}

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
