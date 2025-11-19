package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.ItineraryDTO;
import com.cognizant.demo.entity.Itinerary;
import com.cognizant.demo.exception.NoDataFoundException;

public interface ItineraryInterface {
	    ResponseEntity<ItineraryDTO> createItinerary(ItineraryDTO itinerarydto);
	    ResponseEntity<List<ItineraryDTO>> getAllItineraries() throws NoDataFoundException;
	    ResponseEntity<ItineraryDTO> getItineraryById(int id) throws NoDataFoundException;
	    ResponseEntity<Itinerary> updateItinerary(int id, Itinerary itinerary);
	    ResponseEntity<String> deleteItinerary(int id);
}
