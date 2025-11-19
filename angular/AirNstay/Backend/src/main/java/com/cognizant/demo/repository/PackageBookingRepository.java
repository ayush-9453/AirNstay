package com.cognizant.demo.repository;

import com.cognizant.demo.entity.PackageBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageBookingRepository extends JpaRepository<PackageBooking, String> {

	List<PackageBooking> findByBookingId(String bookingId);
}