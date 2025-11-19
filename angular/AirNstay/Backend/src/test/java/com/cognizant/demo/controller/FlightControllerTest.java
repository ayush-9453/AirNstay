package com.cognizant.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import com.cognizant.demo.dto.FlightDTO;
import com.cognizant.demo.service.FlightService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FlightControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	FlightService serviceObj;
	
	private final String DEP = "mumbai";
	private final String ARR = "delhi";
    private final String DATE = "15/11/2025";
    private final String CLASSTYPE = "economy";
	
	@Test
	public void getFlightSearchedDataTest() throws Exception{
		
		FlightDTO flightData = new FlightDTO("FL902",
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
		
		ResponseEntity<List<FlightDTO>> responseObj = new ResponseEntity<>(List.of(flightData) , HttpStatus.OK);
		
		 when(serviceObj.getFlightSearchedData(DEP, ARR, DATE, CLASSTYPE))
         .thenReturn(responseObj);
		 
		 mockMvc.perform(get("/flight/getFlightSearchedData")
				 .param("dep" , DEP)
				 .param("arr" , ARR)
				 .param("date" , DATE)
				 .param("class" , CLASSTYPE)
				 .contentType(MediaType.APPLICATION_JSON))
		 .andExpect(status().isOk())
		 .andExpect(jsonPath("$.length()").value(1))
		 .andExpect(jsonPath("$[0].flightId").value(flightData.getFlightId()))
		 .andExpect(jsonPath("$[0].departure").value(flightData.getDeparture()))
		 .andExpect(jsonPath("$[0].arrival").value(flightData.getArrival()))
		 .andExpect(jsonPath("$[0].departureDate").value(flightData.getDepartureDate()))
		 .andExpect(jsonPath("$[0].classType").value(flightData.getClassType()));
		 
		 
		 
	}

}
