package com.simple.rest.service.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.simple.rest.service.resources.ConfigConstants;

public class Log {
	
	public static final String DEVELOP_LOGS_FILE_URL = "https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/develop/logs/create_log.php";
	public static final String PRODUCTION_LOGS_FILE_URL = "https://loaiza.000webhostapp.com/Cachi-Fitness-Center-Web-Page/production/logs/create_log.php";
	public static final String LOCALHOST_LOGS_FILE_URL = "http://localhost/projects/write_on_file_php/create_log.php";
	
	public static void create(String tag, String message) {
		String description = Dates.getCurrentDateTime()+": "+tag+" ---> "+message;
		System.out.println(description);
		write(description);
	}
	
	public static void error(String tag, String message) {
		String description = Dates.getCurrentDateTime()+": ERROR in "+tag+" ---> "+message;
		System.out.println(description);
		write(description);
	}
	
	public static void write(String log) {
		
        String requestData = "{\"log\":\""+log+"\"}";
        StringEntity entity = new StringEntity(requestData,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(ConfigConstants.IS_PRODUCTION?PRODUCTION_LOGS_FILE_URL:DEVELOP_LOGS_FILE_URL);
        request.setEntity(entity);

        HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
