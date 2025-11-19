package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.FlightDTO;

import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.FlightService;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/flight")
public class FlightController {
	
	@Autowired
	FlightService flightServiceObj;
	
	@GetMapping("/getFlightSearchedData")
	public ResponseEntity<List<FlightDTO>> getFlightSearchedData(@RequestParam("dep") String departure,
													             @RequestParam("arr") String arrival,
													             @RequestParam("date") String departureDate,
													             @RequestParam("class") String classType
													             ) throws NoDataFoundException {
		
		return flightServiceObj.getFlightSearchedData(departure, arrival, departureDate, classType);
	}
	
	@GetMapping("/getFlightById/{id}")
	public ResponseEntity<FlightDTO> getFlightById(@PathVariable String id) throws NoDataFoundException{
		return flightServiceObj.getFlightById(id);
	}
			

}
