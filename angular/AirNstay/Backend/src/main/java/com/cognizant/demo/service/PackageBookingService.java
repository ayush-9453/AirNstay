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
import com.cognizant.demo.dto.PackageBookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.HotelBooking;
import com.cognizant.demo.entity.PackageBooking;
import com.cognizant.demo.entity.Booking.Status;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.HotelBookingRepository;
import com.cognizant.demo.repository.PackageBookingRepository;

@Service
public class PackageBookingService {
	
	@Autowired
	BookingRepository bookingRepo;

	@Autowired
	PackageBookingRepository packageBookingRepo;

	@Autowired
	ModelMapper modelMap;

	public PackageBookingDTO convertToDto(PackageBooking booking) {
		return modelMap.map(booking, PackageBookingDTO.class);
	}

	public ResponseEntity<List<PackageBookingDTO>> getBookedPackageByUserId(String userId) throws NoDataFoundException {

		List<Booking> bookingDetails = bookingRepo.findByUserId(userId);

		if (bookingDetails.isEmpty()) {
			throw new NoDataFoundException("No bookings found for user...");
		}

		List<PackageBookingDTO> allBookedPackageDtos = new ArrayList<>();

		for (Booking booking : bookingDetails) {
			String bookingId = booking.getBookingId();
			List<PackageBooking> bookedPackageDetails = packageBookingRepo.findByBookingId(bookingId);

			if (!bookedPackageDetails.isEmpty()) {
				List<PackageBookingDTO> bookedPackageDto = bookedPackageDetails.stream().map(this::convertToDto)
						.collect(Collectors.toList());

				allBookedPackageDtos.addAll(bookedPackageDto);
				
				
			}
			
		}

		if (allBookedPackageDtos.isEmpty()) {
			throw new NoDataFoundException("No Hotel bookings found for any booking...");
		}

		return new ResponseEntity<>(allBookedPackageDtos, HttpStatus.OK);

	}
	
	
	public ResponseEntity<String> cancelBookedPackage(String bookingId) throws NoDataFoundException{
		
		Optional<Booking> bookedPackageOp = bookingRepo.findById(bookingId);
		
		if(bookedPackageOp.isPresent()) {
			Booking cancelBooking = bookedPackageOp.get();
			
			cancelBooking.setStatus(Status.cancelled);
			
			bookingRepo.save(cancelBooking);
			
			return new ResponseEntity<>("Hotel Cancelled Successfully" , HttpStatus.OK);
		
		}
		else
			throw new NoDataFoundException("Flight Not Booked To be Cancelled..");
	}

}
