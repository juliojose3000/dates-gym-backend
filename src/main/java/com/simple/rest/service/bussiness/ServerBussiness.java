package com.simple.rest.service.bussiness;

import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.simple.rest.service.util.Utilities;

@Service
public class ServerBussiness {
	
	public String getServerTimeZone() {
		return Utilities.getTimeZoneServer();
	}
	
	public String getServerCurrentTime() {
		return Utilities.getCurrentTimeServer();
	}
	

}
