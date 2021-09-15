package com.simple.rest.service.data;

import java.sql.CallableStatement;
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
import com.simple.rest.service.domain.Reservation;
import com.simple.rest.service.domain.Schedule;
import com.simple.rest.service.domain.Shift;
import com.simple.rest.service.domain.User;
import com.simple.rest.service.resources.Codes;
import com.simple.rest.service.resources.Strings;
import com.simple.rest.service.util.Dates;
import com.simple.rest.service.util.Log;

@Repository
public class ScheduleData {

	private String tableName = "schedule";

	private static DataSource dataSource;

	private static final String TAG = "ScheduleData";

	public static Schedule currentSchedule;

	@Autowired
	ShiftData shiftData;

	@Autowired
	UserData userData;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int getLastCode() throws SQLException {

		int lastCode = -1;
		String query = "select max(id) as id from " + tableName + ";";

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next())
				lastCode = rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		return lastCode;

	}

	public MyResponse create(Schedule schedule) throws SQLException {

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		boolean isSuccessful = true;
		MyResponse mResponse = new MyResponse();

		ArrayList<Shift[]> shiftsList = schedule.getShifts();
		int weekNumber = schedule.getWeekNumber();
		java.sql.Date startDate = Dates.utilDateToSQLDate(schedule.getStartDate());
		java.sql.Date endDate = Dates.utilDateToSQLDate(schedule.getEndDate());

		String query = "insert into " + tableName + "(\r\n" + "start_date, end_date, week_number) \r\n" + "values ("
				+ "'" + startDate + "'," + "'" + endDate + "'," + "" + weekNumber + ");";

		try {
			stmt = conn.createStatement();
			int rs = stmt.executeUpdate(query);

			if (rs != 0) {

				int recentScheduleId = getLastCode();

				for (Shift[] shifts : shiftsList) {

					for (int i = 0; i < shifts.length; i++) {
						isSuccessful = shiftData.create(shifts[i], recentScheduleId);
						if (isSuccessful == false) {
							mResponse.unexpectedErrorResponse();
							break;
						}
					}
				}
			}
			if (isSuccessful) {
				mResponse.setCode(Codes.SUCCESSFUL);
				mResponse.setDescription(Strings.SUCCESSFUL);
				mResponse.setData(schedule);
				mResponse.setSuccessful(true);
				Log.create(TAG,
						"Schedule was created successful = " + schedule.getStartDate() + " - " + schedule.getEndDate());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
			mResponse.unexpectedErrorResponse();
		} finally {
			stmt.close();
			conn.close();
		}

		return mResponse;

	}// method

	public MyResponse getSchedule(int weekNumber) throws SQLException {

		MyResponse mResponse = new MyResponse();

		if (currentSchedule != null) {
			mResponse.successfulResponse();
			mResponse.setData(currentSchedule);
			return mResponse;
		}

		String query = "select * from schedule where week_number = " + weekNumber + ";";

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {
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

				mResponse.successfulResponse();
				mResponse.setData(schedule);

				currentSchedule = schedule;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
			mResponse.unexpectedErrorResponse();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		return mResponse;

	}

	public MyResponse getCurrentSchedule() throws SQLException {

		MyResponse mResponse = new MyResponse();

		if (currentSchedule != null) {
			mResponse.successfulResponse();
			mResponse.setData(currentSchedule);
			return mResponse;
		}

		String query = "SELECT * FROM schedule WHERE id=(SELECT max(id) FROM schedule);";

		Connection conn = dataSource.getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			if (rs.next()) {
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

				mResponse.successfulResponse();
				mResponse.setData(schedule);

				currentSchedule = schedule;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
			mResponse.unexpectedErrorResponse();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}

		return mResponse;

	}

	public void updateScheduleNewReservation(Reservation reservation, int userId) {

		for (Shift[] shifts : currentSchedule.getShifts()) {

			for (Shift shift : shifts) {

				if (Dates.utilDateToString(shift.getDate()).equals(Dates.utilDateToString(reservation.getShiftDate()))
						&& shift.getStartHour().equals(reservation.getShiftStartHour())) {
					shift.setReservedSpace(shift.getReservedSpace() + 1);
					ArrayList<User> customers = shift.getClients();
					try {
						customers.add(userData.findById(userId));
						shift.setClients(customers);
					} catch (SQLException e) {
						e.printStackTrace();
						Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
					}

				}

			}

		}

	}

	public void updateScheduleCancelReservation(Reservation reservation, int userId) {

		for (Shift[] shifts : currentSchedule.getShifts()) {

			for (Shift shift : shifts) {

				if (Dates.utilDateToString(shift.getDate()).equals(Dates.utilDateToString(reservation.getShiftDate()))
						&& shift.getStartHour().equals(reservation.getShiftStartHour())) {
					shift.setReservedSpace(shift.getReservedSpace() - 1);
					ArrayList<User> customers = shift.getClients();
					try {
						customers.remove(userData.findById(userId));
						shift.setClients(customers);
					} catch (SQLException e) {
						e.printStackTrace();
						Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
					}

				}

			}

		}

	}

	public MyResponse deleteLastSchedule() {
		Connection conn;
		MyResponse mResponse = new MyResponse();
		try {
			conn = dataSource.getConnection();
			String callSP = "{call delete_last_schedule()}";
			CallableStatement statement = conn.prepareCall(callSP);
			boolean result = statement.execute();
			if(!result) {
				mResponse.successfulResponse();
			}else {
				mResponse.unexpectedErrorResponse();
			}
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
			Log.error(TAG, e.getMessage(), e.getStackTrace()[0].getLineNumber());
		}
		return mResponse;
	}

}
