package com.cognizant.demo.repository;

import com.cognizant.demo.entity.FlightBooking;
import com.cognizant.demo.entity.HotelBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, String> {

	List<HotelBooking> findByBookingId(String bookingId);
	
}
