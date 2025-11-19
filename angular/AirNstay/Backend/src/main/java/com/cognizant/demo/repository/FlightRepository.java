package com.cognizant.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.demo.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight , String> {

	@Query("SELECT f FROM Flight f WHERE f.departure = :departure AND f.arrival = :arrival AND f.departureDate = :departureDate AND f.classType = :classType")
	List<Flight> findByUserSearchDetail(
	    @Param("departure") String departure,
	    @Param("arrival") String arrival,
	    @Param("departureDate") String departureDate,
	    @Param("classType") String classType
	);

}


