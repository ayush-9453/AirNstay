package com.cognizant.demo.dto;

import java.util.List;

import com.cognizant.demo.entity.DayItinerary;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryDTO {
	
	private int itineraryId;
	private List<String> imagesUrls;
	private List<DayItinerary> dayItinerary;
}
