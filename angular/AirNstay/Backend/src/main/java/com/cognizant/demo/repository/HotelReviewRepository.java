package com.cognizant.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.demo.entity.HotelReview;

public interface HotelReviewRepository extends JpaRepository<HotelReview , String> {

	List<HotelReview> findByHotelId(String hotelId);

	

}
