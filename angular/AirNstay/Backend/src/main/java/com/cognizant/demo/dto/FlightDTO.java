package com.cognizant.demo.dto;


import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
	
	@Id
	private String flightId;
	
 
    private String flightLogo;
	
    private String airline;
    
    private String departure;
    
    private String arrival;
    
    private String departureDate;
    
    private String arrivalDate;
    
    private double price;
    
    private String availability;
    
    private String departureTime;
    
    private String arrivalTime;
    
    private String duration;
    
    private String classType;

}
