package com.cognizant.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Booking {

    @Id
    @NotNull(message="bookingId cannot be null")
    private String bookingId;

    @NotNull(message = "UserId is not available")
    private String userId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "booking_date")
    @NotNull
    private LocalDateTime bookingDate;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Traveller> travellers;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private ContactInfo contactInfo;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    public enum Type {
        flight, packages, hotel
    }

    public enum Status {
        confirmed, cancelled
    }    
    
}
