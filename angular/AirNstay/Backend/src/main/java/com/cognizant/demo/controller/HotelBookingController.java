package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.HotelBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.HotelBookingService;

@RestController
@RequestMapping("/booking")
public class HotelBookingController {
	
	@Autowired
	HotelBookingService hotelBookingServiceObj;
	
	@GetMapping("/getBookedHotelByUserId/{userId}")
	public ResponseEntity<List<HotelBookingDTO>> getBookedFlightByUserId(@PathVariable String userId) throws NoDataFoundException{
		return hotelBookingServiceObj.getBookedHotelByUserId(userId);
	}
	
	@PostMapping("/cancelBookedHotel/{bookingId}")
	public ResponseEntity<String> cancelBookedFlight(@PathVariable String bookingId) throws NoDataFoundException{
		return hotelBookingServiceObj.cancelBookedHotel(bookingId);
	}


}
