package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.PackageBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.PackageBookingService;

@RestController
@RequestMapping("/booking")
public class PackageBookingController {

	@Autowired
	PackageBookingService packageBookingServiceObj;
	
	@GetMapping("/getBookedPackageByUserId/{userId}")
	public ResponseEntity<List<PackageBookingDTO>> getBookedFlightByUserId(@PathVariable String userId) throws NoDataFoundException{
		return packageBookingServiceObj.getBookedPackageByUserId(userId);
	}
	
	@PostMapping("/cancelBookedPackage/{bookingId}")
	public ResponseEntity<String> cancelBookedFlight(@PathVariable String bookingId) throws NoDataFoundException{
		return packageBookingServiceObj.cancelBookedPackage(bookingId);
	}
}
