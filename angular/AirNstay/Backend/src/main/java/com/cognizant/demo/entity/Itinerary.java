package com.cognizant.demo.entity;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Itinerary {
	
	@Id
	@NotNull(message = "Id cannot be null")
	private int itineraryId;
	
	@ElementCollection
    @Column(length = 1024)
	private List<String> imagesUrls;
	

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "itinerary_id") // This creates the foreign key in DayItinerary
    private List<DayItinerary> dayItinerary;
	

}
