package com.simple.rest.service.data;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSOutput;

import com.simple.rest.service.domain.LinkResetPassword;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.ResetPassword;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.ConfigConstants;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.EncryptionPasswords;
import com.simple.rest.service.util.Log;

@Repository
public class UserData {

	private DataSource dataSource;

	private String tableName = "user";

	public static ArrayList<User> LIST_USERS = new ArrayList<>();
	
	private static final String TAG = "UserData";

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ArrayList<User> getAll() throws SQLException {

		String query = "SELECT * FROM " + tableName;
		User user;
		ArrayList<User> listUsers = new ArrayList<>();

		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String email = rs.getString("email");

				user = new User();
				user.setId(id);
				user.setName(name);
				user.setPhoneNumber(phone);
				user.setEmail(email);

				listUsers.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}

		rs.close();
		stmt.close();
		conn.close();
		return listUsers;

	}

	public void loadUsers() throws SQLException {

		if (LIST_USERS.size() != 0)
			LIST_USERS = new ArrayList<>();
		String query = "SELECT * FROM " + tableName;
		User user;

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				byte[] salt = rs.getBytes("salt");
				byte[] passwordWithSalt = rs.getBytes("password_with_salt");
				boolean isEnabled = rs.getBoolean("is_enabled");

				user = new User();
				user.setId(id);
				user.setName(name);
				user.setPhoneNumber(phone);
				user.setEmail(email);
				user.setSalt(salt);
				user.setPasswordWithSalt(passwordWithSalt);
				user.setEnabled(isEnabled);

				LIST_USERS.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}

		rs.close();
		stmt.close();
		conn.close();

	}

	public MyResponse create(User user) throws SQLException, NoSuchAlgorithmException {

		Connection conn = dataSource.getConnection();

		MyResponse mResponse = new MyResponse();

		String name = user.getName();
		String phone = user.getPhoneNumber();
		String email = user.getEmail();
		String password = user.getPassword();
		byte[] salt = EncryptionPasswords.generateSalt();
		byte[] passwordWithSalt = EncryptionPasswords.getHashWithSalt(password, salt);
		boolean isEnabled = false; //Initially, the user has its account disabled

		String query = "insert into " + tableName + "(name, phone, email, salt, password_with_salt, is_enabled) values (?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, phone);
		pstmt.setString(3, email);
		pstmt.setBytes(4, salt);
		pstmt.setBytes(5, passwordWithSalt);
		pstmt.setBoolean(6, isEnabled);

		try {
			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.USER_CREATED_SUCCESSFUL);
				mResponse.setDescription(Strings.USER_CREATED_SUCCESSFUL);
				loadUsers();
				Log.create(TAG, "New user created. Name = "+name+", email = "+email);
			}
		} catch (SQLException e) {

			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);

			switch (e.getErrorCode()) {
				case Codes.DUPLICATE_ENTRY_ERROR:
					mResponse.setDescription(Strings.DUPLICATE_ENTRY_USER_ERROR);
					break;
				default:
					e.printStackTrace();
					Log.create(TAG, e.getMessage());
					mResponse.unexpectedErrorResponse();
					break;
			}

		}
		pstmt.close();
		conn.close();
		return mResponse;
	}

	public User findById(int id) throws SQLException {
		User user = null;
		if (LIST_USERS.size() == 0) {
			loadUsers();// I load de users from db
			Log.create(this.getClass().getName(), "Lista de usuarios vacía");
		}
		for (User userItem : LIST_USERS) {
			if (userItem.getId() == id) {
				user = userItem;
				break;
			}
		}
		return user;
	}

	public User findByEmail(String email) throws SQLException {
		User user = null;
		if (LIST_USERS.size() == 0) {
			loadUsers();// I load de users from db
			Log.create(this.getClass().getName(), "Lista de usuarios vacía... Procediendo a cargarla");
		}
		for (User userItem : LIST_USERS) {
			if (userItem.getEmail().equals(email)) {
				user = new User(userItem);
				break;
			}
		}
		return user;
	}
	

	public boolean doesUserExists(String email) throws SQLException {
		boolean doesUserExists = false;
		if (LIST_USERS.size() == 0) {
			loadUsers();// I load de users from db
			Log.create(this.getClass().getName(), "Lista de usuarios vacía... Procediendo a cargarla");
		}
		for (User userItem : LIST_USERS) {
			if (userItem.getEmail().equals(email)) {
				doesUserExists = true;
				break;
			}
		}
		return doesUserExists;
	}

	public MyResponse generateLinkResetPassword(LinkResetPassword linkResetPassword) throws SQLException {

		MyResponse mResponse = new MyResponse();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			String code = linkResetPassword.getCode();
			String userEmail = linkResetPassword.getUserEmail();
			String expireTime = linkResetPassword.getExpireTime();
			String expireDate = linkResetPassword.getExpireDate();
			int usedLink = linkResetPassword.isUsedLink();

			String query = "insert into link_reset_password (\r\n" + "code, user_email, expire_date, expire_time, used_link) \r\n"
					+ "values (" + "'" + code + "'," + "'" + userEmail + "'," + "'" + expireDate + "'," + "'"
					+ expireTime + "'," + "'" + usedLink + "');";

			int rs = stmt.executeUpdate(query);
			if (rs != 0) {
				mResponse.successfulResponse();
				mResponse.setCode(Codes.LINK_RESET_PASSWORD_CREATED);
				mResponse.setDescription(Strings.LINK_RESET_PASSWORD_CREATED);
				Log.create(TAG, "Link to reset password created for "+ userEmail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		stmt.close();
		conn.close();
		return mResponse;

	}

	public LinkResetPassword getResetPasswordLinkByCode(String code) throws SQLException {

		String query = "select * from link_reset_password where code = '" + code + "';";
		LinkResetPassword linkResetPassword = null;

		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		try {
			if (rs.next()) {
				int id = rs.getInt("id");
				String userEmail = rs.getString("user_email");
				String expireDate = rs.getString("expire_date");
				String expireTime = rs.getString("expire_time");
				int usedLink = rs.getInt("used_link");

				linkResetPassword = new LinkResetPassword();
				linkResetPassword.setId(id);
				linkResetPassword.setUserEmail(userEmail);
				linkResetPassword.setCode(code);
				linkResetPassword.setExpireDate(expireDate);
				linkResetPassword.setExpireTime(expireTime);
				linkResetPassword.setUsedLink(usedLink);

			}

		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			return null;
		}

		rs.close();
		stmt.close();
		conn.close();

		return linkResetPassword;

	}

	public MyResponse resetPassword(ResetPassword resetPassword) throws SQLException {

		Log.create(TAG, "Reset password for "+resetPassword.getUserEmail());
		
		MyResponse mResponse = new MyResponse();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();

			String newPassword = resetPassword.getNewPassword();
			String userEmail = resetPassword.getUserEmail();
			byte[] salt = EncryptionPasswords.generateSalt();
			byte[] newPasswordWithSalt = EncryptionPasswords.getHashWithSalt(newPassword, salt);

			String query = "update user set salt = ?, password_with_salt = ? where email = '" + userEmail + "';";
			pstmt = conn.prepareStatement(query);
			pstmt.setBytes(1, salt);
			pstmt.setBytes(2, newPasswordWithSalt);
			
			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				mResponse.successfulResponse();
				mResponse.setCode(Codes.PASSWORD_UPDATE_SUCCESSFUL);
				mResponse.setDescription(Strings.PASSWORD_UPDATE_SUCCESSFUL);
				if(!changeResetPasswordLinkState(resetPassword).isSuccessful()) {
					mResponse.unexpectedErrorResponse();
					Log.create(TAG, "An error has occurred updating password for "+userEmail);
				}
				else {
					loadUsers();
					Log.create(TAG, "Password updated successfully for "+userEmail);	
				}

			}
		} catch (SQLException | NoSuchAlgorithmException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		} 
		pstmt.close();
		conn.close();
		return mResponse;

	}
	
	public MyResponse changeResetPasswordLinkState(ResetPassword resetPassword) throws SQLException {

		MyResponse mResponse = new MyResponse();
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			String userEmail = resetPassword.getUserEmail();
			String resetPasswordLinkCode = resetPassword.getResetLinkCode();
			int usedLink = ConfigConstants.TRUE;

			String query = "update link_reset_password set used_link = "+usedLink+" where user_email = '"+userEmail+"' and code = '"+resetPasswordLinkCode+"';";

			int rs = stmt.executeUpdate(query);
			if (rs != 0) {
				mResponse.successfulResponse();
			}
		} catch (SQLException e) {
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);
			e.printStackTrace();
            Log.error(TAG, e.getMessage());

		}
		stmt.close();
		conn.close();
		return mResponse;

	}

	public boolean resetLinkHasBeenUsed(String code, String expireDate, String expireTime) throws SQLException {
		
		String query = "select used_link from link_reset_password where code = '"+code+"' and expire_date = '"+expireDate+"' and expire_time = '"+expireTime+"';";

		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		int usedLink = ConfigConstants.FALSE;
		try {
			if (rs.next()) 
				usedLink = rs.getInt("used_link");
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		rs.close();
		stmt.close();
		conn.close();
		return usedLink==ConfigConstants.TRUE;
		
	}

	public MyResponse updateUserProfile(User user, String newPassword) throws SQLException, NoSuchAlgorithmException {
		Log.create(TAG, "Update profile by "+user.getName() + "["+user.getEmail()+"]");
		Connection conn = dataSource.getConnection();

		MyResponse mResponse = new MyResponse();

		String name = user.getName();
		String phone = user.getPhoneNumber();
		String email = user.getEmail();
		String currentPassword = user.getPassword();
		byte[] currentSalt = findByEmail(email).getSalt();
		byte[] currentPasswordWithSalt = EncryptionPasswords.getHashWithSalt(currentPassword, currentSalt);

		String query;
		PreparedStatement pstmt = null;
		if(!newPassword.equals("null")) {//This means the user update its password
			byte[] newSalt = EncryptionPasswords.generateSalt();
			byte[] newPasswordWithSalt = EncryptionPasswords.getHashWithSalt(newPassword, newSalt);
			query = "update " + tableName + " set name = ?, phone = ?, email = ?, salt = ?, password_with_salt = ? where id ="+user.getId() + " AND password_with_salt = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);
			pstmt.setBytes(4, newSalt);
			pstmt.setBytes(5, newPasswordWithSalt);
			pstmt.setBytes(6, currentPasswordWithSalt);
		}
		else {
			query = "update " + tableName + " set name = ?, phone = ?, email = ? where id ="+user.getId() + " AND password_with_salt = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, phone);
			pstmt.setString(3, email);
			pstmt.setBytes(4, currentPasswordWithSalt);
		}
		
		try {
			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				Log.create(TAG, "Profile has been updated successfully");
				mResponse.successfulResponse();
				mResponse.setDescription(Strings.USER_PROFILE_UPDATED_SUCCESSFUL);
				user.setPassword(""); //It's not secure return the password
				mResponse.setData(user);
				loadUsers();
			}else {
				Log.create(TAG, "User password is wrong");
				mResponse.errorResponse();
				mResponse.setDescription(Strings.WRONG_PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		pstmt.close();
		conn.close();
		return mResponse;
	}

	public MyResponse enableUserAccount(String userEmail) throws SQLException {
		
		MyResponse mResponse = new MyResponse();
		
		if(findByEmail(userEmail).isEnabled()) {
			mResponse.errorResponse();
			mResponse.setDescription(Strings.USER_ACCOUNT_IS_ALREADY_ENABLED);
			return mResponse;
		}
		
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			String query = "update user set is_enabled = true where email = '"+userEmail+"';";

			int rs = stmt.executeUpdate(query);
			if (rs != 0) {
				mResponse.successfulResponse();
				mResponse.setDescription(Strings.USER_ACCOUNT_ENABLED_SUCCESSFUL);	
				User user = findByEmail(userEmail);
				user.setEnabled(true);
				enableUserInList(user);
				Log.create(TAG, "User account has been enabled for "+user.getName() + "["+user.getEmail()+"]");
			}
		} catch (SQLException e) {
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);
			e.printStackTrace();
            Log.error(TAG, e.getMessage());

		}
		stmt.close();
		conn.close();
		return mResponse;
		
	}

	public boolean userIsEnabled(String email) {
		boolean userIsEnabled = false;
		try {
			User user = findByEmail(email);
			userIsEnabled = user.isEnabled();
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		return userIsEnabled;
	}
	
	private User enableUserInList(User user) throws SQLException {

		if (LIST_USERS.size() == 0) {
			loadUsers();// I load de users from db
			Log.create(this.getClass().getName(), "Lista de usuarios vacía... Procediendo a cargarla");
		}
		for (User userItem : LIST_USERS) {
			if (userItem.getEmail().equals(user.getEmail())) {
				userItem.setEnabled(user.isEnabled());
				break;
			}
		}
		return user;
	}

	public MyResponse registerUserPhone(User user) throws SQLException {
		Connection conn = dataSource.getConnection();

		MyResponse mResponse = new MyResponse();

		String query = "update " + tableName + " set phone = ? where email ="+user.getEmail() + ";";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, user.getPhoneNumber());

		try {
			int rs = pstmt.executeUpdate();
			if (rs != 0) {
				mResponse.successfulResponse();
				mResponse.setCode(Codes.USER_CREATED_SUCCESSFUL);
				mResponse.setDescription(Strings.USER_CREATED_SUCCESSFUL);
				mResponse.setData(user);
				loadUsers();
			}else {
				mResponse.errorResponse();
				mResponse.setDescription(Strings.WRONG_PASSWORD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
			mResponse.unexpectedErrorResponse();
		}
		pstmt.close();
		conn.close();
		return mResponse;
	}
	

}
