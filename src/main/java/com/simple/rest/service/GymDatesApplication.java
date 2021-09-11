package com.simple.rest.service;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.simple.rest.service.resources.ConfigConstants;
import com.simple.rest.service.resources.TimeZoneStrings;
import com.simple.rest.service.util.Log;


@SpringBootApplication
public class GymDatesApplication {
	
	public static final String TAG = "GymDatesApplication";

	public static void main(String[] args) {
		try {
			String configConstantsValues = Log.createWithoutWrite(TAG, "Loading config constants...");
			URL url = new URL(ConfigConstants.IS_PRODUCTION?ConfigConstants.PRODUCTION_CONFIG_FILE_URL:ConfigConstants.DEVELOP_CONFIG_FILE_URL);
			Scanner scanner = new Scanner(url.openStream());
			
			configConstantsValues += Log.createWithoutWrite(TAG, "IS_PRODUCTION="+ConfigConstants.IS_PRODUCTION);	
			// read from your scanner
			while (scanner.hasNextLine()) {
				String configConstant = scanner.nextLine();
				String[] keyValue = configConstant.split("=");
				configConstantsValues += Log.createWithoutWrite(TAG, keyValue[0]+"="+ keyValue[1]);
				ConfigConstants.setValues(keyValue[0], keyValue[1]);
			}
			scanner.close();
			if(ConfigConstants.PRINT_CONFIG_CONSTANTS_VALUES)
				Log.write(configConstantsValues);

			
	        SpringApplication application = new SpringApplication(GymDatesApplication.class);

	        Properties properties = new Properties();
	        properties.put("spring.datasource.url", ConfigConstants.getDataBaseConnectionString());
	        application.setDefaultProperties(properties);

	        application.run(args);
			
		} catch (IOException ex) {
			// there was some connection problem, or the file did not exist on the server,
			// or your URL was not in the right format.
			// think about what to do now, and put it here.
			ex.printStackTrace(); // for now, simply output it.
		}

	}

	@PostConstruct
	public void init() {
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone(TimeZoneStrings.COSTA_RICA));
	}

}
