package com.cognizant.demo.service;

import java.util.*;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.ItineraryDTO;
import com.cognizant.demo.entity.Itinerary;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.ItineraryRepository;
import com.cognizant.demo.serviceImp.ItineraryInterface;

@Service
public class ItineraryService implements ItineraryInterface{
	
	@Autowired
	ItineraryRepository repo;
	
	@Autowired
	private ModelMapper model;
	
	public ItineraryDTO convertToDto(Itinerary itinerary) {
	        return model.map(itinerary, ItineraryDTO.class);
	    }

	public Itinerary convertToEntity(ItineraryDTO itineraryDTO) {
	        return model.map(itineraryDTO, Itinerary.class);
	  }

	@Override
	public ResponseEntity<ItineraryDTO> createItinerary(ItineraryDTO itinerarydto) {
		Itinerary itinerary = convertToEntity(itinerarydto);
		Itinerary itObj = repo.save(itinerary);
		return new ResponseEntity<>(convertToDto(itObj),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<ItineraryDTO>> getAllItineraries() throws NoDataFoundException {
		List<Itinerary> itineraryLst = repo.findAll();
		if(!itineraryLst.isEmpty()) {
			List<ItineraryDTO> lstDto = itineraryLst.stream().map(this::convertToDto).toList();
			return new ResponseEntity<>( lstDto,HttpStatus.ACCEPTED);
		}else {
		   throw new NoDataFoundException("No Itinerary found!");
		}
		
	}

	@Override
	public ResponseEntity<ItineraryDTO> getItineraryById(int id) throws NoDataFoundException {
		Optional<Itinerary> itn = repo.findById(id);
		if(!itn.isEmpty()) {
			ItineraryDTO dto = convertToDto(itn.get());
			return new ResponseEntity<>(dto,HttpStatus.ACCEPTED);
		}else {
			throw new NoDataFoundException("No itinerary found with this Id");
		}
	
	}

	@Override
	public ResponseEntity<Itinerary> updateItinerary(int id, Itinerary itinerary) {
		Itinerary existing = repo.findById(id).orElse(null);
        if (existing != null) {
            itinerary.setItineraryId(id);
            return new ResponseEntity<>(repo.save(itinerary),HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<String> deleteItinerary(int id) {
		repo.deleteById(id);
		return new ResponseEntity<>("Delete Successfull",HttpStatus.OK);
		
	}
	
	

}
