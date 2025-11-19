package com.cognizant.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;
import com.cognizant.demo.service.HotelReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HotelReviewService hotelReviewServiceObj;

	@Autowired
	private ObjectMapper objectMap;

	@Test
	public void testInsertHotelReview() throws Exception {

		HotelReview review = new HotelReview("R001", "U001", "H001", "Abhay", 4.5, "Great stay!",
				"2025-10-25T10:00:00");


		when(hotelReviewServiceObj.insertHotelReviewById(any(HotelReview.class)))
		.thenReturn(ResponseEntity.ok(review));


		mockMvc.perform(post("/hotel/insertHotelReview").contentType(MediaType.APPLICATION_JSON)
				.content(objectMap.writeValueAsString(review)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reviewId").value("R001"))
				.andExpect(jsonPath("$.name").value("Abhay"))
				.andExpect(jsonPath("$.rating").value(4.5));
	}

	@Test
	public void testGetAllReviewByHotelId() throws Exception {
		String hotelId = "H001";

		HotelReviewDTO reviewDto = new HotelReviewDTO("Abhay", 4.5, "Great stay!", "2025-10-25T10:00:00");

		List<HotelReviewDTO> reviewList = List.of(reviewDto);

		when(hotelReviewServiceObj.getAllReviewByHotelId(hotelId)).thenReturn(ResponseEntity.ok(reviewList));

		mockMvc.perform(get("/hotel/getAllReviewByHotelId/{hotelId}", hotelId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Abhay"))
			.andExpect(jsonPath("$[0].rating").value(4.5))
			.andExpect(jsonPath("$[0].comment").value("Great stay!"));
	}

}
