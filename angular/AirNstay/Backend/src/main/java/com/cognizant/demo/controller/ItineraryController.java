package com.cognizant.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.cognizant.demo.dto.ItineraryDTO;
import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.serviceImp.ItineraryInterface;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/itinerary")
@RestController
public class ItineraryController {
	
	@Autowired
	ItineraryInterface service;
	
	@PostMapping("/insert")
	public ResponseEntity<ItineraryDTO> createItinerary(@Valid @RequestBody ItineraryDTO itinerarydto) {
        return service.createItinerary(itinerarydto);
		}

	@GetMapping("/all")
	public ResponseEntity<List<ItineraryDTO>> getAll() throws NoDataFoundException {
		return service.getAllItineraries();
		}

	@GetMapping("/{id}")
	public ResponseEntity<ItineraryDTO> getById(@PathVariable int id) throws NoDataFoundException{
		return service.getItineraryById(id);
		}

	@PutMapping("/update/{id}")
	public ResponseEntity<Itinerary> update(@Valid @PathVariable int id, @RequestBody Itinerary itinerary) {
		return service.updateItinerary(id, itinerary);
	  	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable int id) {
		return service.deleteItinerary(id);
	    }

}
