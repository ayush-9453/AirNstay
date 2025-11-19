package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.FlightBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IFlightBooking {
	
	public ResponseEntity<List<FlightBookingDTO>> getBookedFlightByUserId(String userId) throws NoDataFoundException;
	
	public ResponseEntity<String> cancelBookedFlight(String bookingId) throws NoDataFoundException;

}
