package com.cognizant.demo.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class HotelBooking extends Booking {
	
	private String hotelId;
    private String name;
    private String checkInDate;
    private String checkOutDate; 
    private Integer numberOfGuests;
    private String address;
    private String city;
    
}