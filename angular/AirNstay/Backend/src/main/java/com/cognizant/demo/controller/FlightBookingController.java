package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.FlightBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.FlightBookingService;

@RestController
@RequestMapping("/booking")
public class FlightBookingController {
	
	@Autowired
	FlightBookingService flightBookingServiceObj; 
	
	@GetMapping("/getBookedFlightByUserId/{userId}")
	public ResponseEntity<List<FlightBookingDTO>> getBookedFlightByUserId(@PathVariable String userId) throws NoDataFoundException{
		return flightBookingServiceObj.getBookedFlightByUserId(userId);
	}
	
	@PostMapping("/cancelBookedFlight/{bookingId}")
	public ResponseEntity<String> cancelBookedFlight(@PathVariable String bookingId) throws NoDataFoundException{
		return flightBookingServiceObj.cancelBookedFlight(bookingId);
	}

}
