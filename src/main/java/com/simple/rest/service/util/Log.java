package com.simple.rest.service.util;

public class Log {
	
	public static void create(String tag, String message) {
		System.out.println(Dates.getCurrentDateTime()+": "+tag+" ---> "+message);
	}

}
