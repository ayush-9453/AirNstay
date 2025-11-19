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
public class Flight {

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
