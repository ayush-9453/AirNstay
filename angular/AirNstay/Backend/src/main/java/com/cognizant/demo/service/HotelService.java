package com.cognizant.demo.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.demo.dto.HotelDTO;
import com.cognizant.demo.entity.Hotel;
import com.cognizant.demo.entity.HotelBenefits;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.HotelRepository;
import com.cognizant.demo.serviceImp.IHotel;

@Service
public class HotelService implements IHotel {
	
	@Autowired
	HotelRepository hotelRepo;
	
	@Autowired
	ModelMapper modelMap;
	
	public HotelDTO convertToDto(Hotel hotel) {
	    HotelDTO hotelDto = modelMap.map(hotel , HotelDTO.class); 
	  
	    HotelBenefits benefits = hotel.getBenefits();
	    if(benefits != null) {
	        hotelDto.setCustomerSupport(benefits.getCustomerSupport());
	        hotelDto.setEarlyCheckIn(benefits.getEarlyCheckIn());
	        hotelDto.setFlexibleCancellation(benefits.getFlexibleCancellation());
	        hotelDto.setFreeBreakfast(benefits.getFreeBreakfast());
	        hotelDto.setLoyaltyRewards(benefits.getLoyaltyRewards());
	    }
	    
	    return hotelDto;
	}
	
	@Transactional(readOnly = true)
	public ResponseEntity<List<HotelDTO>> getByLocation(String city) throws NoDataFoundException{
		
		List<Hotel> hotelDataObj = hotelRepo.findByCity(city.toLowerCase());
		
		if(!hotelDataObj.isEmpty()) {
			List<HotelDTO> hotelData = hotelDataObj.stream()
					.map(this :: convertToDto)
					.toList();
			
			return new ResponseEntity<>(hotelData , HttpStatus.OK);
		}
		else
			throw new NoDataFoundException("No Data Found In The Database...");
		
	}

	
	public ResponseEntity<List<HotelDTO>> getHotelById(String hotelId) throws NoDataFoundException {
		
		Optional<Hotel> hotelDetailsObj =  hotelRepo.findById(hotelId); 
		
		if(!hotelDetailsObj.isEmpty()){
			List<HotelDTO> hotelDetails = hotelDetailsObj.stream()
					.map(this :: convertToDto)
					.toList();
			
			return new ResponseEntity<> (hotelDetails , HttpStatus.OK);
		}
		else
			throw new NoDataFoundException("No Hotel Found in DataBase With This Id...");

	}

}
