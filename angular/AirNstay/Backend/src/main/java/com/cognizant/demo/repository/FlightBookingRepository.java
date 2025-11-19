package com.cognizant.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.FlightBooking;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, String> {
	
	List<FlightBooking> findByBookingId(String id);
}

