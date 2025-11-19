package com.cognizant.demo.dto;

import java.util.List;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
	
	@Id
	private String hotelId;
    private String name;
    
    private String hotelImage1;
    private String hotelImage2; 
    private String hotelImage3;
    
    private String address;
    private String city;
    private String pincode;
    private String landmark;
    private String mapLink;

    private int roomsAvailable;    
    private double pricePerNight;
    
    private String amenities;

    private String description;

    private String earlyCheckIn;
    private String freeBreakfast;
    private String flexibleCancellation;
    private String customerSupport;
    private String loyaltyRewards;



}
