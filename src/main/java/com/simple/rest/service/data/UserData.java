package com.simple.rest.service.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;

@Repository
public class UserData {
	
	private static DataSource dataSource;
	
	private String tableName = "user";
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public ArrayList<User> getAll() throws SQLException {
		
		Connection  conn = dataSource.getConnection();
		
		String query = "SELECT * FROM "+tableName;

		User user;
		
		ArrayList<User> listUsers = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				int id = rs.getInt("date");
				String name = rs.getString("name");
				String lastName = rs.getString("lastname");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				
				user = new User();
				user.setId(id);
				user.setName(name);
				user.setLastName(lastName);
				user.setPhoneNumber(phone);
				user.setEmail(email);		
				
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
