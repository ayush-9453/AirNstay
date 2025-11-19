package com.cognizant.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.Hotel;
import com.cognizant.demo.entity.HotelBenefits;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class HotelRepositoryTest {
	
	@Autowired
	HotelRepository hotelRepo;
	
	private Hotel hotel;
	private HotelBenefits benefits;

    @BeforeEach
    public void setup() {
		MockitoAnnotations	.openMocks(this);
		
		hotel = new Hotel(
		        "H001", "Vivanta Coimbatore", "image1.jpg", "image2.jpg", "image3.jpg",
		        "123 Race Course Road", "coimbatore", "641018", "Near Railway Station",
		        "https://maps.example.com/vivanta", 10, 3500.0,
		        String.join(" , " , Arrays.asList("WiFi", "Pool", "Gym", "Spa")),
		        "Luxury hotel in the heart of Coimbatore",
		        null
		    ); 
		
		benefits = new HotelBenefits();
	    benefits.setEarlyCheckIn("Yes");
	    benefits.setFreeBreakfast("Included");
	    benefits.setFlexibleCancellation("Flexible");
	    benefits.setCustomerSupport("24/7");
	    benefits.setLoyaltyRewards("Gold Member");
	    benefits.setHotel(hotel);
	    
	   
    }
	
	@Test
	public void getHotelByCity() throws Exception{
		
		hotel.setBenefits(benefits);
	    hotelRepo.save(hotel);

	    List<Hotel> hotelDataOp = hotelRepo.findByCity("coimbatore");

	    assertEquals("H001", hotelDataOp.get(0).getHotelId());
	    assertEquals("coimbatore", hotelDataOp.get(0).getCity());
	    assertEquals("Yes", hotelDataOp.get(0).getBenefits().getEarlyCheckIn());
	}
	
	@Test
	public void getHotelByHotelId() throws Exception{
		
		hotel.setBenefits(benefits);
	    hotelRepo.save(hotel);

	    Optional<Hotel> hotelDataOp = hotelRepo.findById("H001");

	    assertTrue(hotelDataOp.isPresent(), "Hotel should be found");
	    assertEquals("H001", hotelDataOp.get().getHotelId());
	    assertEquals("coimbatore", hotelDataOp.get().getCity());
	    assertEquals("Yes", hotelDataOp.get().getBenefits().getEarlyCheckIn());
	}

}
