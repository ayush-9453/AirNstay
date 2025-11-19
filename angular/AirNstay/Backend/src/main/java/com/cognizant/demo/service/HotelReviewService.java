package com.cognizant.demo.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;
import com.cognizant.demo.exception.DataAlreadyFoundExcecption;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.HotelReviewRepository;
import com.cognizant.demo.serviceImp.IHotelReview;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HotelReviewService implements IHotelReview {
	
	@Autowired
	HotelReviewRepository reviewRepo;
	
	@Autowired
	ModelMapper modelMap;
	
	public HotelReviewDTO convertToDto (HotelReview reviewObj ) {
		return modelMap.map(reviewObj , HotelReviewDTO.class);
	}
	
	public ResponseEntity<HotelReview> insertHotelReviewById(HotelReview reviewObj) throws DataAlreadyFoundExcecption {
			
			Optional<HotelReview> hotelReviewObj = reviewRepo.findById(reviewObj.getReviewId()); 
			if(hotelReviewObj.isEmpty()) 
				return new ResponseEntity<>(reviewRepo.save(reviewObj) , HttpStatus.OK);
			else
				throw new DataAlreadyFoundExcecption("Review Can not be saved! ");
			
	}
	
	public ResponseEntity<List<HotelReviewDTO>> getAllReviewByHotelId(String hotelId) throws NoDataFoundException{
		
		List<HotelReview> hotelReviewObj = reviewRepo.findByHotelId(hotelId);
		
		if(!hotelReviewObj.isEmpty()) {
			List<HotelReviewDTO> hotelReview = hotelReviewObj.stream()
					.map(this :: convertToDto)
					.toList();
			
			log.info("Review are :" +hotelReview);
			return new ResponseEntity<> (hotelReview , HttpStatus.OK);
		}
		else
			throw new NoDataFoundException("There is no review found in the database..." +hotelId);
	}
	
	

}
