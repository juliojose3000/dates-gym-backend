package com.simple.rest.service.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Random;

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
	
	public static String getTimeWithFormat() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		return timeFormat.format(c.getTime());
	}
	
	public static String alphaNumericRandom(int randomLength) {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = randomLength;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	
	public static String getHostname() throws UnknownHostException {
		
		return InetAddress.getLocalHost().getHostName();
		
	}
	

}
