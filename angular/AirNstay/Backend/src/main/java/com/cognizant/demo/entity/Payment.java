package com.cognizant.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Payment {
   
	@Id
    private String paymentId;
    private String userId;
    private BigDecimal amount;
    private String status; 
    private String method; 
    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}

