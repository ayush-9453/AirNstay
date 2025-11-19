package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.FlightDTO;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IFlight {
	public ResponseEntity<List<FlightDTO>> getFlightSearchedData(String departure, String arrival, 
																String departureDate, String classType) throws NoDataFoundException;

}
