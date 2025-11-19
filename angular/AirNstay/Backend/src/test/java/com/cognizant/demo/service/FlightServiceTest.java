package com.cognizant.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.FlightDTO;
import com.cognizant.demo.entity.Flight;
import com.cognizant.demo.repository.FlightRepository;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

	@Mock
	FlightRepository flightRepo;
	
	@InjectMocks
	FlightService serviceObj;
	
	@Mock
	ModelMapper modelMap;
	
	public FlightDTO convertToDto (Flight flight) {
		return modelMap.map(flight, FlightDTO.class);
	}
	
	
	private final String DEP = "mumbai";
    private final String ARR = "delhi";
    private final String DATE = "15/11/2025"; 
    private final String CLASSTYPE = "economy";
    
    private Flight flightData;
    private FlightDTO expectedDto;
    
    
    @BeforeEach
    void setup() {
    	flightData = new Flight("FL902",
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
	            "CLASSTYPE"
	        );
    	
    	expectedDto = new FlightDTO("FL902",
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
    }
    
	@Test
    void getSearchedFlightData() throws Exception{
    	
    	when(modelMap.map(flightData , FlightDTO.class )).thenReturn(expectedDto);
    	
    	when(flightRepo.findByUserSearchDetail(DEP, ARR, DATE, CLASSTYPE)).thenReturn(List.of(flightData));
    	
    	ResponseEntity<List<FlightDTO>> responseFlightData = serviceObj.getFlightSearchedData(DEP, ARR, DATE, CLASSTYPE);
    	
    	FlightDTO finalFlightData = responseFlightData.getBody().get(0);
    	assertEquals("FL902" , finalFlightData.getFlightId() );
    	assertEquals(DEP , finalFlightData.getDeparture() );
    	assertEquals(ARR , finalFlightData.getArrival() );
    	assertEquals(DATE , finalFlightData.getDepartureDate() );
    	assertEquals(CLASSTYPE , finalFlightData.getClassType() );
    }
    
}
