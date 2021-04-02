package com.simple.rest.service.resources;

public class ConfigConstants {
	
	public static final boolean PRODUCTION = false; //TODO put in true to production
	public static final int HOUR_TO_CREATE_A_NEW_SCHEDULE = 17; //5 PM
	public static final String JSEGURAC_PC = "jsegurac-pc";
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	public static final String CFC_URL_PRODUCTION = "https://cachi-fitness-center.web.app";
	public static final String CFC_URL_DEVELOP = "http://192.168.88.110:4200";
	public static final String CFC_GMAIL = "juliojose3000@gmail.com";
	
	public static final boolean IGNORE_TOKEN_EXPIRATION = true;//This variable indicates if the token (user session) has a life time
	public static final int TOKEN_MINUTES_LIFE_TIME = 60;//This variable indicates, if the previus variable is true, the life time of a user session.

	
}
