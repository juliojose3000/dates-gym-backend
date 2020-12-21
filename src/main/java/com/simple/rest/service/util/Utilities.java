package com.simple.rest.service.util;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;

public class Utilities {
	
	public static String getTimeZoneServer(){
		ZoneId z = ZoneId.systemDefault() ;
		String myTimeZone = z.toString();
		return myTimeZone;
	}
	
	public static String getCurrentTimeServer() {
		SimpleDateFormat format = new SimpleDateFormat("E dd/MM/yyyy HH:mm:ss");
		Calendar c = Calendar.getInstance();
		return format.format(c.getTime());
	}

}
