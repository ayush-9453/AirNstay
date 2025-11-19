package com.cognizant.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, String>{
	
	List<Booking> findByUserId(String userId);

}
