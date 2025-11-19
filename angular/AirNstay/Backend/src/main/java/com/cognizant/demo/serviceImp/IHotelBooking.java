package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.HotelBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IHotelBooking {
	
	public ResponseEntity<List<HotelBookingDTO>> getBookedHotelByUserId(String userId) throws NoDataFoundException;

	public ResponseEntity<String> cancelBookedHotel(String bookingId) throws NoDataFoundException;
}
