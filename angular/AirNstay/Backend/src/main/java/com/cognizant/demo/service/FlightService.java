	package com.cognizant.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.FlightDTO;
import com.cognizant.demo.entity.Flight;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.FlightRepository;
import com.cognizant.demo.serviceImp.IFlight;

@Service
public class FlightService implements IFlight{
	
	@Autowired
	FlightRepository flightRepo;
	
	@Autowired
	ModelMapper modelMap; 
	
	public FlightDTO convertToDto(Flight flight) {
		return modelMap.map(flight, FlightDTO.class);
	}
	
	
	public ResponseEntity<List<FlightDTO>> getFlightSearchedData(String departure, String arrival, 
															String departureDate, String classType) throws NoDataFoundException{
		
		List<Flight> flightSearch = flightRepo.findByUserSearchDetail(departure.toLowerCase() , arrival.toLowerCase() , departureDate.toLowerCase() , classType.toLowerCase());
		
		if(!flightSearch.isEmpty()) {
			List<FlightDTO> flightDtoList = flightSearch.stream()
			        .map(this::convertToDto)
			        .toList();
			return new ResponseEntity<>(flightDtoList,HttpStatus.OK);
		}
		else
			throw new NoDataFoundException("No Data Found in the Database...");
	}


	public ResponseEntity<FlightDTO> getFlightById(String id) throws NoDataFoundException {
		Optional<Flight> flight=flightRepo.findById(id);
		if(!flight.isEmpty()) {
			FlightDTO flightDto = convertToDto(flight.get());
			return new ResponseEntity<>(flightDto,HttpStatus.OK);
		}
		else {
			throw new NoDataFoundException("No Data Found with this ID");
		}
	}
	


}
