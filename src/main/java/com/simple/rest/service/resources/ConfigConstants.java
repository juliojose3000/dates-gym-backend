package com.simple.rest.service.resources;

public class ConfigConstants {


	/*
	 * public static final boolean PRODUCTION = false; //TODO put in true to
	 * production public static final int HOUR_TO_CREATE_A_NEW_SCHEDULE = 17; //5 PM
	 * public static final String JSEGURAC_PC = "jsegurac-pc"; public static final
	 * int TRUE = 1; public static final int FALSE = 0; 
	 * public static final String RONTEND_URL_PRODUCTION = "https://cachi-fitness-center.web.app"; 
	 * public static final String FRONTEND_URL_DEVELOP = "http://192.168.88.14:4200";
	 * public static final String BACKEND_URL_PRODUCTION = "https://gym-dates-backend.herokuapp.com/gymcachi"; 
	 * public static final String BACKEND_URL_DEVELOP = "http://192.168.88.14:8080/gymcachi"; public
	 * static final String CFC_GMAIL = "juliojose3000@gmail.com";
	 * 
	 * public static final boolean IGNORE_TOKEN_EXPIRATION = true;//This variable indicates if the token (user session) has a life time 
	 * public static final int TOKEN_MINUTES_LIFE_TIME = 60;//This variable indicates, if the previus variable is false, the life time of a user session. 
	 * public static final boolean SEND_EMAIL = false;
	 */

	public static final String DEVELOP_CONFIG_FILE_URL = "https://loaizagreen.000webhostapp.com/Cachi-Fitness-Center-Web-Page/develop/config/config.txt";
	public static final String PRODUCTION_CONFIG_FILE_URL = "https://loaizagreen.000webhostapp.com/Cachi-Fitness-Center-Web-Page/production/config/config.txt";
	public static final boolean IS_PRODUCTION = false; // TODO put in true to production
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	public static int HOUR_TO_CREATE_A_NEW_SCHEDULE; // 5 PM
	public static String FRONTEND_URL;
	public static String BACKEND_URL;
	public static String SERVER_EMAIL;
	public static String SERVER_EMAIL_PASSWORD;
	public static boolean IGNORE_TOKEN_EXPIRATION;// This variable indicates if the token (user session) has a life time
	public static int TOKEN_MINUTES_LIFE_TIME;// This variable indicates, if the previus variable is false, the life time of a user session.
	public static boolean SEND_EMAIL;
	public static int NUMBER_OF_CUSTOMERS_PER_SHIFT;
	public static boolean PRINT_CONFIG_CONSTANTS_VALUES;
	public static String DATABASE_NAME;
	public static String DB_USERNAME;
	public static String DB_PASSWORD;

	public static void setValues(String key, String value) {
		switch (key) {
		case "HOUR_TO_CREATE_A_NEW_SCHEDULE":
			HOUR_TO_CREATE_A_NEW_SCHEDULE = Integer.parseInt(value);
			break;
		case "FRONTEND_URL":
			FRONTEND_URL = value;
			break;
		case "BACKEND_URL":
			BACKEND_URL = value;
			break;
		case "SERVER_EMAIL":
			SERVER_EMAIL = value;
			break;
		case "SERVER_EMAIL_PASSWORD":
			SERVER_EMAIL_PASSWORD = value;
			break;
		case "IGNORE_TOKEN_EXPIRATION":
			IGNORE_TOKEN_EXPIRATION = Boolean.parseBoolean(value);
			break;
		case "TOKEN_MINUTES_LIFE_TIME":
			TOKEN_MINUTES_LIFE_TIME = Integer.parseInt(value);
			break;
		case "SEND_EMAIL":
			SEND_EMAIL = Boolean.parseBoolean(value);
			break;
		case "NUMBER_OF_CUSTOMERS_PER_SHIFT":
			NUMBER_OF_CUSTOMERS_PER_SHIFT = Integer.parseInt(value);
			break;
		case "PRINT_CONFIG_CONSTANTS_VALUES":
			PRINT_CONFIG_CONSTANTS_VALUES = Boolean.parseBoolean(value);
			break;
		case "DATABASE_NAME":
			DATABASE_NAME = value;
			break;
		case "DB_USERNAME":
			DB_USERNAME = value;
			break;
		case "DB_PASSWORD":
			DB_PASSWORD = value;
			break;
		}
	}
	
	public static String getDataBaseConnectionString() {
		return "jdbc:mysql://sql5.freemysqlhosting.net:3306/"+DATABASE_NAME;
	}

}
