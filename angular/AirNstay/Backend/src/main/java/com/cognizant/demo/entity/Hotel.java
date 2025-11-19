package com.cognizant.demo.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
	
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
    
    @OneToOne (mappedBy = "hotel" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private HotelBenefits benefits;

}
