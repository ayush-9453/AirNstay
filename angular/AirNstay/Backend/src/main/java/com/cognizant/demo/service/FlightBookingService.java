package com.cognizant.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.FlightBookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.Booking.Status;
import com.cognizant.demo.entity.FlightBooking;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.FlightBookingRepository;
import com.cognizant.demo.serviceImp.IFlightBooking;

@Service
public class FlightBookingService implements IFlightBooking {

	@Autowired
	BookingRepository bookingRepo;

	@Autowired
	FlightBookingRepository flightBookingRepo;

	@Autowired
	ModelMapper modelMap;

	public FlightBookingDTO convertToDto(FlightBooking booking) {
		return modelMap.map(booking, FlightBookingDTO.class);
	}

	public ResponseEntity<List<FlightBookingDTO>> getBookedFlightByUserId(String userId) throws NoDataFoundException {

		List<Booking> bookingDetails = bookingRepo.findByUserId(userId);

		if (bookingDetails.isEmpty()) {
			throw new NoDataFoundException("No bookings found for user...");
		}

		List<FlightBookingDTO> allBookedFlightDtos = new ArrayList<>();

		for (Booking booking : bookingDetails) {
			String bookingId = booking.getBookingId();
			List<FlightBooking> bookedFlightDetails = flightBookingRepo.findByBookingId(bookingId);

			if (!bookedFlightDetails.isEmpty()) {
				List<FlightBookingDTO> bookedFlightDto = bookedFlightDetails.stream().map(this::convertToDto)
						.collect(Collectors.toList());

				allBookedFlightDtos.addAll(bookedFlightDto);
				
				
			}
			
		}

		if (allBookedFlightDtos.isEmpty()) {
			throw new NoDataFoundException("No flight bookings found for any booking...");
		}

		return new ResponseEntity<>(allBookedFlightDtos, HttpStatus.OK);

	}
	
	
	public ResponseEntity<String> cancelBookedFlight(String bookingId) throws NoDataFoundException{
		
		Optional<Booking> bookedFlightOp = bookingRepo.findById(bookingId);
		
		if(bookedFlightOp.isPresent()) {
			Booking cancelBooking = bookedFlightOp.get();
			
			cancelBooking.setStatus(Status.cancelled);
			
			bookingRepo.save(cancelBooking);
			
			return new ResponseEntity<>("Flight Cancelled Successfully" , HttpStatus.OK);
		
		}
		else
			throw new NoDataFoundException("Flight Not Booked To be Cancelled..");
	}
	
	

}
