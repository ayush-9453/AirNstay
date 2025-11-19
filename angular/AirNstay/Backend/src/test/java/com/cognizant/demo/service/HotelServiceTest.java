package com.cognizant.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.dto.FlightDTO;
import com.cognizant.demo.dto.HotelDTO;
import com.cognizant.demo.entity.Hotel;
import com.cognizant.demo.entity.HotelBenefits;
import com.cognizant.demo.repository.HotelRepository;


@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	HotelRepository hotelRepo;
	
	@Mock
	ModelMapper modelMap;

	@InjectMocks
	HotelService hotelServiceObj;
	
	
	
	private Hotel hotel;
	private HotelBenefits benefits;
	private HotelDTO hotelDto;

@BeforeEach
    public void setup() {
	MockitoAnnotations	.openMocks(this);
        
        benefits = new HotelBenefits(
    			"H001"	,"Yes", "Included", "Flexible", "24/7", "Gold Member" , hotel);

    	hotel = new Hotel(
    		    "H001", "Vivanta Coimbatore", "image1.jpg", "image2.jpg", "image3.jpg",
    		    "123 Race Course Road", "coimbatore", "641018", "Near Railway Station",
    		    "https://maps.example.com/vivanta", 10, 3500.0,
    		    String.join(", ", Arrays.asList("WiFi", "Pool", "Gym", "Spa")),
    		    "Luxury hotel in the heart of Coimbatore",
    		    benefits
    		);

    	hotelDto = new HotelDTO(
    	    "H001", "Vivanta Coimbatore", "image1.jpg", "image2.jpg", "image3.jpg",
    	    "123 Race Course Road", "coimbatore", "641018", "Near Railway Station",
    	    "https://maps.example.com/vivanta", 10, 3500.0,
    	    String.join(", ", Arrays.asList("WiFi", "Pool", "Gym", "Spa")),
    	    "Luxury hotel in the heart of Coimbatore",
    	    "Yes", "Included", "Flexible", "24/7", "Gold Member"
    	);
}
	
	@Test
	public void getHotelByCity() throws Exception{
	
	String city = "coimbatore";	

	when(modelMap.map(hotel, HotelDTO.class)).thenReturn(hotelDto);

	when(hotelRepo.findByCity(city)).thenReturn(List.of(hotel));
    
    ResponseEntity<List<HotelDTO>> response = hotelServiceObj.getByLocation(city);

	assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, response.getBody().size());
    assertEquals("H001", response.getBody().get(0).getHotelId());
    assertEquals("coimbatore", response.getBody().get(0).getCity());


	}
	
	@Test
	public void getHotelByHotelId() throws Exception{
	
	String hotelId = "H001";	

	when(modelMap.map(hotel, HotelDTO.class)).thenReturn(hotelDto);

	when(hotelRepo.findById(hotelId)).thenReturn(Optional.of(hotel));
    
    ResponseEntity<List<HotelDTO>> response = hotelServiceObj.getHotelById(hotelId);

	assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, response.getBody().size());
    assertEquals("H001", response.getBody().get(0).getHotelId());
    assertEquals("coimbatore", response.getBody().get(0).getCity());


	}
}
