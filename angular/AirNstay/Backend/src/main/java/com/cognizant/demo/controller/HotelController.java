package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.HotelDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.HotelService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/hotel")
public class HotelController {

	
	@Autowired
	HotelService hotelServiceObj;
	
	@GetMapping ("/results/{city}") 
	public ResponseEntity<List<HotelDTO>> getHotelSearchedData(@PathVariable String city) throws NoDataFoundException {
		return  hotelServiceObj.getByLocation(city);
	}
	
	@GetMapping("/details/{hotelId}")
	public ResponseEntity<List<HotelDTO>> getHotelById(@PathVariable String hotelId) throws NoDataFoundException {
		return hotelServiceObj.getHotelById(hotelId);
	}
	
	
}
