package com.cognizant.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary,Integer> {

}
