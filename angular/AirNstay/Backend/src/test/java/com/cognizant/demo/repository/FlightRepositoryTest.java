package com.cognizant.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.Flight;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class FlightRepositoryTest {
	
	@Autowired
	FlightRepository flightRepo;
	
	private final String DEP = "mumbai";
	private final String ARR = "delhi";
    private final String DATE = "15/11/2025";
    private final String CLASSTYPE = "economy";
    
    @Test
    public void getSearchedFlightData() throws Exception{
    	
    	Flight flightData = new Flight("FL902",
	            "/logos/airindia.png",
	            "Air India",
	            DEP,
	            ARR,
	            DATE,
	            "15/11/2025",
	            8500.50,
	            "Available",
	            "14:30",
	            "16:45",
	            "2h 15m",
	            CLASSTYPE
	        );
    	
    	flightRepo.save(flightData);
    	
    	List<Flight> searchedFlightData = flightRepo.findByUserSearchDetail(DEP, ARR, DATE, CLASSTYPE);
		
    	assertEquals(1 , searchedFlightData.size());
    	assertEquals("FL902" , searchedFlightData.get(0).getFlightId());
    	assertEquals(DEP , searchedFlightData.get(0).getDeparture());
    	assertEquals(ARR , searchedFlightData.get(0).getArrival());
    	assertEquals(DATE , searchedFlightData.get(0).getDepartureDate());
    	assertEquals(CLASSTYPE , searchedFlightData.get(0).getClassType());
    }

}
