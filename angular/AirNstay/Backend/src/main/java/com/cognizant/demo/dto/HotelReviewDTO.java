
package com.cognizant.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelReviewDTO {
	
	
	private String name;
	
	private double rating;
	
	private String comment;
	
	private String timestamp;


}
