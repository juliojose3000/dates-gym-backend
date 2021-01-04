package com.simple.rest.service.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.Binnacle;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.util.Dates;

@Repository
public class BinnacleData {
	
	private static DataSource dataSource;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public MyResponse getAll() throws SQLException{
		
		String query = "select * from binnacle;";
		ArrayList<Binnacle> binnacleRecords = new ArrayList<>();
		
		Connection  conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		MyResponse mResponse = new MyResponse();
		mResponse.successfulResponse();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			Binnacle binnacle;
			while(rs.next()) {
				binnacle = new Binnacle();
				binnacle.setId(rs.getInt("id"));
				binnacle.setAction(rs.getString("action"));
				binnacle.setDate(Dates.stringToUtilDate(rs.getString("date")));
				binnacle.setTime(rs.getString("time"));
				binnacleRecords.add(binnacle);
			}
			mResponse.setData(binnacleRecords);
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
		}
		rs.close();
		stmt.close();
		conn.close();
		return mResponse;
		
	}
	
	public MyResponse addRecord(Binnacle binnacle) throws SQLException {
		MyResponse mResponse = new MyResponse();
		Connection conn = null;
		Statement stmt = null;
		String query = "insert into binnacle(action, date, time) "
				+ "values ('"+binnacle.getAction()+"','"+binnacle.getSQLDate()+"','"+binnacle.getTime()+"');";
		System.out.print(query);
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			int rs = stmt.executeUpdate(query);
			if (rs != 0) 
				mResponse.successfulResponse();
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
		}
		
		stmt.close();
		conn.close();
		return mResponse;	
	}
	

}
