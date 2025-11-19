package com.cognizant.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String bookingId;
    private String userId;
    private BookingType type;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private List<TravellerDTO> travellers;
    private ContactInfoDTO contactInfo;
    private PaymentDTO payment;

    public enum BookingType {
        flight, packages, hotel
    }

    public enum BookingStatus {
        confirmed, cancelled
    }
}

