package com.simple.rest.service.data;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.simple.rest.service.domain.Shift;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShiftDataTest {
	
	@Autowired
	ShiftData shiftData;
	
	@Test
	public void getShift() throws SQLException {
		
		int idSchedule = 6;
		
		ArrayList<Shift[]> listShift = shiftData.get(idSchedule);	
		
		System.out.println(listShift.toString());
		
		assertTrue(listShift.size()>0);
		
	}

}
