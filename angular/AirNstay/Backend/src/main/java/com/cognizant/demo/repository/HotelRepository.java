package com.cognizant.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel , String>{

	List<Hotel> findByCity(String city);

}
