package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.HotelDTO;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IHotel {
	
	public ResponseEntity<List<HotelDTO>> getByLocation(String location) throws NoDataFoundException;

	ResponseEntity<List<HotelDTO>> getHotelById(String hotelId) throws NoDataFoundException;

}
