package com.simple.rest.service.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Log;

@Repository
public class ReservationData {

	public DataSource dataSource;

	public static Connection conn;

	private String tableName = "reservation";

	@Lazy
	@Autowired
	UserData userData;

	public static boolean IT_IS_MAKING_RESERVATION = false;

	public static final String TAG = "ReservationData";

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public MyResponse make(Reservation reservation, int userId) throws SQLException, InterruptedException {

		User user = userData.findById(userId);
		String username = user.getEmail();

		boolean aux = true;
		while (IT_IS_MAKING_RESERVATION) {
			if (aux) {
				Log.create(TAG, username + " - Otra sesión está usando la conexión, esperando a que finalice...");
				aux = false;
			}
			TimeUnit.SECONDS.sleep(1);
		}
		IT_IS_MAKING_RESERVATION = true;

		Log.create(TAG, username + " - Procediendo con la reservación...");

		conn = dataSource.getConnection();
		Statement stmt = null;

		MyResponse mResponse = new MyResponse();

		if (user == null) {
			IT_IS_MAKING_RESERVATION = false;
			mResponse.unexpectedErrorResponse();
			mResponse.setCode(Codes.AN_ERROR_HAS_OCCURRED_LOGIN_AGAIN);
			mResponse.setDescription(Strings.AN_ERROR_HAS_OCCURRED_LOGIN_AGAIN);
			Log.create(TAG, username + " - usuario no encontrado, cancelando reservación");
			return mResponse;
		}

		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();
		String shiftEndHour = reservation.getShiftEndHour();

		Log.create(TAG, "Making reservation by " + user.getName() + "[" + user.getEmail() + "]" + " on "
				+ Dates.utilDateToString(shiftDate) + " at " + shiftStartHour);

		// I added this condicional, because store procedures ignores checks clauses and
		// trigger doesn´t work in GCP
		if (!thereIsAvailableSpace(shiftDate, shiftStartHour)) {
			Log.create(TAG, Strings.NO_AVAILABLE_SPACE);
			mResponse.errorResponse();
			mResponse.setDescription(Strings.NO_AVAILABLE_SPACE);
			conn.close();
			IT_IS_MAKING_RESERVATION = false;
			return mResponse;
		}

		try {
			stmt = conn.createStatement();
			String query = "insert into reservation(id_user, date_shift, start_hour_shift, end_hour_shift) values (" + "'"
					+ user.getId() + "','" + Dates.utilDateToString(shiftDate) + "','" + shiftStartHour + "','" + shiftEndHour + "');";

			int rs = stmt.executeUpdate(query);

			if (rs != 0) {
				Log.create(TAG, "Reservation made successfully");
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.RESERVATION_SUCCESSFUL);
				mResponse.setDescription(Strings.RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);

				String callSP = "{call update_available_space('" + Dates.utilDateToString(shiftDate) + "', '"
						+ shiftStartHour + "')}";
				CallableStatement statement = conn.prepareCall(callSP);
				statement.execute();

			}

		} catch (SQLException e) {
			mResponse.setSuccessful(false);
			mResponse.setCode(e.getErrorCode());
			mResponse.setTitle(Strings.ERROR);

			switch (e.getErrorCode()) {
			case Codes.DUPLICATE_ENTRY_ERROR:
				mResponse.setDescription(Strings.DUPLICATE_ENTRY_RESERVATION_ERROR);
				Log.error(TAG, Strings.DUPLICATE_ENTRY_RESERVATION_ERROR, e.getStackTrace()[0].getLineNumber());
				break;
			case Codes.NO_AVAILABLE_SPACE:
				mResponse.setDescription(Strings.NO_AVAILABLE_SPACE);
				Log.error(TAG, Strings.NO_AVAILABLE_SPACE, e.getStackTrace()[0].getLineNumber());
				break;
			default:
				e.printStackTrace();
				Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
				mResponse.unexpectedErrorResponse();
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
			mResponse.unexpectedErrorResponse();
		} finally {
			IT_IS_MAKING_RESERVATION = false;
			stmt.close();
			conn.close();
		}

		return mResponse;

	}

	public MyResponse cancel(Reservation reservation, int userId) throws SQLException {

		Connection conn = dataSource.getConnection();
		Statement stmt = null;

		MyResponse mResponse = new MyResponse();

		User user = userData.findById(userId);
		Date shiftDate = reservation.getShiftDate();
		String shiftStartHour = reservation.getShiftStartHour();

		String query = "delete from reservation where id_user = " + user.getId() + " AND date_shift = '"
				+ Dates.utilDateToString(shiftDate) + "';";

		try {
			stmt = conn.createStatement();

			int rs = stmt.executeUpdate(query);
			Log.create(TAG, "Cancel reservation by " + user.getName() + "[" + user.getEmail() + "]" + " on "
					+ Dates.utilDateToString(shiftDate) + " at " + shiftStartHour);

			if (rs != 0) {
				Log.create(TAG, "Reservation canceled successfully");
				mResponse.setSuccessful(true);
				mResponse.setCode(Codes.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setDescription(Strings.CANCEL_RESERVATION_SUCCESSFUL);
				mResponse.setTitle(Strings.SUCCESSFUL);

				String callSP = "{call cancel_reservation(" + user.getId() + ", '" + Dates.utilDateToString(shiftDate)
						+ "', '" + shiftStartHour + "')}";
				CallableStatement statement = conn.prepareCall(callSP);
				statement.execute();

			}
		} catch (Exception e) {
			Log.create(TAG, " - An error has occurred with " + user.getName() + "'s reservation");
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
			mResponse.unexpectedErrorResponse();
		} finally {
			stmt.close();
			conn.close();
		}

		return mResponse;

	}

	public ArrayList<User> getClients(String date, String startHour) throws SQLException {

		String query = "select id_user from reservation where date_shift = '" + date + "' and start_hour_shift = '"
				+ startHour + "';";
		ArrayList<User> listUsers = new ArrayList<>();

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("id_user");
				User user = userData.findById(id);
				listUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		return listUsers;

	}

	public boolean thereIsAvailableSpace(Date shiftDate, String shiftStartHour) throws SQLException {

		String query = "SELECT available_space FROM shift WHERE shift_date=" + "'" + Dates.utilDateToString(shiftDate)
				+ "' AND start_hour='" + shiftStartHour + "';";

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		int availableSpace = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				availableSpace = rs.getInt("available_space");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}
		return availableSpace == 0 ? false : true;

	}

	public ArrayList<Reservation> getCustomerReservations(int userId) throws SQLException {

		//String query = "select * from reservation where id_user = " + userId + ";";
		String query = "select * from reservation r, schedule s where r.id_user = "+userId+" and s.id=(SELECT max(s.id) FROM schedule) and r.date_shift >= s.start_date;";
		ArrayList<Reservation> listReservations = new ArrayList<>();

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			int id = rs.getInt("id_user");
			String dateShift = rs.getString("date_shift");
			String startHourShift = rs.getString("start_hour_shift");
			String endHourShift = rs.getString("end_hour_shift");

			Reservation reservation = new Reservation();
			reservation.setShiftDate(Dates.stringToUtilDate(dateShift));
			reservation.setShiftStartHour(startHourShift);
			reservation.setShiftEndHour(endHourShift);

			listReservations.add(reservation);
		}

		rs.close();
		stmt.close();
		conn.close();

		return listReservations;

	}

}
