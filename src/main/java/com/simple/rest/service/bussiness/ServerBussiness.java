package com.simple.rest.service.bussiness;

import java.time.ZoneId;

import org.springframework.stereotype.Service;

@Service
public class ServerBussiness {
	
	public String getTimeZone() {
		ZoneId z = ZoneId.systemDefault() ;
		String myTimeZone = z.toString();
		return myTimeZone;
	}
	

}
