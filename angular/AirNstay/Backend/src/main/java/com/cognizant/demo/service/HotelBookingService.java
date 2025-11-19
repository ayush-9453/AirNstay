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

import com.cognizant.demo.dto.HotelBookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.HotelBooking;
import com.cognizant.demo.entity.Booking.Status;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.FlightBookingRepository;
import com.cognizant.demo.repository.HotelBookingRepository;
import com.cognizant.demo.serviceImp.IHotelBooking;

@Service
public class HotelBookingService implements IHotelBooking {
	
	@Autowired
	BookingRepository bookingRepo;

	@Autowired
	HotelBookingRepository hotelBookingRepo;

	@Autowired
	ModelMapper modelMap;

	public HotelBookingDTO convertToDto(HotelBooking booking) {
		return modelMap.map(booking, HotelBookingDTO.class);
	}

	public ResponseEntity<List<HotelBookingDTO>> getBookedHotelByUserId(String userId) throws NoDataFoundException {

		List<Booking> bookingDetails = bookingRepo.findByUserId(userId);

		if (bookingDetails.isEmpty()) {
			throw new NoDataFoundException("No bookings found for user...");
		}

		List<HotelBookingDTO> allBookedHotelDtos = new ArrayList<>();

		for (Booking booking : bookingDetails) {
			String bookingId = booking.getBookingId();
			List<HotelBooking> bookedHotelDetails = hotelBookingRepo.findByBookingId(bookingId);

			if (!bookedHotelDetails.isEmpty()) {
				List<HotelBookingDTO> bookedFlightDto = bookedHotelDetails.stream().map(this::convertToDto)
						.collect(Collectors.toList());

				allBookedHotelDtos.addAll(bookedFlightDto);
				
				
			}
			
		}

		if (allBookedHotelDtos.isEmpty()) {
			throw new NoDataFoundException("No Hotel bookings found for any booking...");
		}

		return new ResponseEntity<>(allBookedHotelDtos, HttpStatus.OK);

	}
	
	
	public ResponseEntity<String> cancelBookedHotel(String bookingId) throws NoDataFoundException{
		
		Optional<Booking> bookedHotelOp = bookingRepo.findById(bookingId);
		
		if(bookedHotelOp.isPresent()) {
			Booking cancelBooking = bookedHotelOp.get();
			
			cancelBooking.setStatus(Status.cancelled);
			
			bookingRepo.save(cancelBooking);
			
			return new ResponseEntity<>("Hotel Cancelled Successfully" , HttpStatus.OK);
		
		}
		else
			throw new NoDataFoundException("Flight Not Booked To be Cancelled..");
	}

}
