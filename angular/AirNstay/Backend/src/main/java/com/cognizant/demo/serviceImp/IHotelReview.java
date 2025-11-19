package com.cognizant.demo.serviceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;
import com.cognizant.demo.exception.DataAlreadyFoundExcecption;
import com.cognizant.demo.exception.NoDataFoundException;

public interface IHotelReview {
	
	public ResponseEntity<HotelReview> insertHotelReviewById(HotelReview reviewObj) throws DataAlreadyFoundExcecption;
	
	public ResponseEntity<List<HotelReviewDTO>> getAllReviewByHotelId(String hotelId) throws NoDataFoundException;

}
