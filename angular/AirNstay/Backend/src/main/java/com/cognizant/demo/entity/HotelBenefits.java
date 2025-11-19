package com.cognizant.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelBenefits {
	
	 @Id
	 @Column(name = "hotel_id")
	 private String hotelId; 
	 
	private String earlyCheckIn;
    private String freeBreakfast;
    private String flexibleCancellation;
    private String customerSupport;
    private String loyaltyRewards;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

}
