package com.simple.rest.service.bussiness;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.rest.service.data.BinnacleData;
import com.simple.rest.service.domain.MyResponse;
import com.simple.rest.service.util.Log;

@Service
public class BinnacleBussiness {
	
	@Autowired
	BinnacleData binnacleData;
	
	private static final String TAG = "BinnacleBussiness";
	
	public MyResponse get() {
		
		MyResponse mResponse = new MyResponse();
		try {
			mResponse = binnacleData.getAll();
		} catch (SQLException e) {
			mResponse.unexpectedErrorResponse();
			e.printStackTrace();
            Log.error(TAG, e.getMessage());
		}
		return mResponse;
		
	}

}
