package com.cognizant.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelReview {
	
	@Id
	private String reviewId;
	
	private String userId;
	
	private String hotelId;
	
	private String name;
	
	private double rating;
	
	private String comment;
	
	private String timestamp;

}
