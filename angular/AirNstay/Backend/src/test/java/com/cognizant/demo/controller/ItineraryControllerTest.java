package com.cognizant.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.dto.ItineraryDTO;
import com.cognizant.demo.entity.DayItinerary;
import com.cognizant.demo.serviceImp.ItineraryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ItineraryControllerTest {
	
	@MockitoBean
	private ItineraryInterface service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	
	@Test
	void getallItineraryTest() throws Exception{
		List<ItineraryDTO> mockdto = List.of(new ItineraryDTO(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner"))));
		ResponseEntity<List<ItineraryDTO>> responseEntity = new ResponseEntity<>(mockdto , HttpStatus.OK)	;
		
		when(service.getAllItineraries()).thenReturn(responseEntity);
		
		mockMvc.perform(get("/itinerary/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].dayItinerary[0].title").value("Day1"));

	}
	
	@Test
	void findItineraryByIdTest() throws Exception {
		ItineraryDTO mockdto = new ItineraryDTO(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner")));
		
		ResponseEntity<ItineraryDTO> responseEntity = new ResponseEntity<>(mockdto,HttpStatus.OK);
		
		when(service.getItineraryById(1)).thenReturn(responseEntity);
		
		mockMvc.perform(get("/itinerary/{id}",1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dayItinerary[0].title").value("Day1"));
	}
	
	@Test 
	void insertItineraryTest() throws Exception{
		ItineraryDTO mockdto = new ItineraryDTO(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner")));
		
		ResponseEntity<ItineraryDTO> responseEntity = new ResponseEntity<>(mockdto , HttpStatus.OK);
		
		when(service.createItinerary(mockdto)).thenReturn(responseEntity);
		
		mockMvc.perform(post("/itinerary/insert")
		        .contentType("application/json")
		        .content(objectMapper.writeValueAsString(mockdto)))
		    .andExpect(status().isOk())
		    .andExpect(jsonPath("$.dayItinerary[0].title").value("Day1"));

	}
	
	@Test
	void deleteItineraryTest() throws Exception{
		new ItineraryDTO(1,
				List.of("img1.jpg","img2.jpg","img3.jpg"),
				List.of(new DayItinerary(1,1,"Day1", "Arrival", "Breakfast at hotel","Taj Hotel", "Dinner")));
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>("" , HttpStatus.OK);
		
		when(service.deleteItinerary(1)).thenReturn(responseEntity);
		
		mockMvc.perform(delete("/itinerary/delete/{id}",1))
		    .andExpect(status().isOk());
	}
}
