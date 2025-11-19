package com.cognizant.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;
import com.cognizant.demo.exception.DataAlreadyFoundExcecption;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.HotelReviewService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/hotel")
public class ReviewController {
	
	@Autowired
	HotelReviewService hotelReviewServiceObj;
	
	@PostMapping("/insertHotelReview")
	public ResponseEntity<HotelReview> insertHotelReview(@RequestBody HotelReview reviewObj) throws DataAlreadyFoundExcecption{
		
		return hotelReviewServiceObj.insertHotelReviewById(reviewObj);
	}
	
	@GetMapping("/getAllReviewByHotelId/{hotelId}")
	public ResponseEntity<List<HotelReviewDTO>> getAllReviewByHotelId(@PathVariable String hotelId) throws NoDataFoundException{
		
		return hotelReviewServiceObj.getAllReviewByHotelId(hotelId);
	}

}
