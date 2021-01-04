package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.BinnacleData;
import com.simple.rest.service.domain.MyResponse;

@Service
public class BinnacleBussiness {
	
	@Autowired
	BinnacleData binnacleData;
	
	public MyResponse get() {
		
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = binnacleData.getAll();
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
		}
		return mResponse;
		
	}

}
