package com.cognizant.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class FlightBooking extends Booking {
    private String flightId;
    private String airline;
    private String departure;
    private String arrival;
    private String departureDate;
    private String arrivalDate;
    private String duration;
    private String classType;
}
