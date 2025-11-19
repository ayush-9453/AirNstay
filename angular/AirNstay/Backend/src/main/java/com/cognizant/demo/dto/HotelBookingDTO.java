package com.cognizant.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HotelBookingDTO extends BookingDTO {
    private String hotelId;
    private String name;
    private String checkInDate;
    private String checkOutDate;
    private Integer numberOfGuests;
    private String address;
    private String city;
     
}

