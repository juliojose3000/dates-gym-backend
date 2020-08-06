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
import com.simple.rest.service.util.Dates;

@Repository
public class UserData {

	private DataSource dataSource;

	private String tableName = "user";

	public static ArrayList<User> LIST_USERS = new ArrayList<>();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ArrayList<User> getAll() throws SQLException {

		Connection conn = dataSource.getConnection();

		String query = "SELECT * FROM " + tableName;

		User user;

		ArrayList<User> listUsers = new ArrayList<>();

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				int id = rs.getInt("id");
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

	public void load() throws SQLException {

		Connection conn = dataSource.getConnection();

		String query = "SELECT * FROM " + tableName;

		User user;

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				int id = rs.getInt("id");
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

				LIST_USERS.add(user);

			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean create(User user) throws SQLException {

		Connection conn = dataSource.getConnection();

		boolean wasSuccessfulProcess = false;

		int id = user.getId();
		String name = user.getName();
		String lastname = user.getLastName();
		String phone = user.getPhoneNumber();
		String email = user.getEmail();

		String query = "insert into " + tableName + "(\r\n" + "name, lastname, phone, email) \r\n" + "values (" + "'"
				+ name + "'," + "'" + lastname + "'," + "'" + phone + "'," + "'" + email + "');";

		try {

			Statement stmt = conn.createStatement();

			int rs = stmt.executeUpdate(query);

			if (rs != 0) {
				wasSuccessfulProcess = true;
			}

			stmt.close();

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return wasSuccessfulProcess;

	}

	public User findById(int id) throws SQLException {
		User user = null;
		if(LIST_USERS.size()==0) {
			load();//I load de users from db
			System.out.println("Lista de usuarios vacía");
		}
		for (User userItem : LIST_USERS) {
			if (userItem.getId() == id) {
				user = userItem;
				break;
			}
		}
		return user;
	}

}
