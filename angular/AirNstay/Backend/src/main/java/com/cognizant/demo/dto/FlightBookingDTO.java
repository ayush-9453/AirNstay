package com.cognizant.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FlightBookingDTO extends BookingDTO {
    private String flightId;
    private String airline;
    private String departure;
    private String arrival;
    private String departureDate;
    private String arrivalDate;
    private String duration;
    private String classType;
}
