package com.cognizant.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayItinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int day;

    @NotBlank
    private String title;

    @Size(max = 1000)
    private String description;

    private String mealsIncluded;
    private String overnightStay;
    private String highlights;


}
