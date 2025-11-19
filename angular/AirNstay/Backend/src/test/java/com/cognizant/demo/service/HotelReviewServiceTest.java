package com.cognizant.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;
import com.cognizant.demo.repository.HotelReviewRepository;

@ExtendWith(MockitoExtension.class)
public class HotelReviewServiceTest {
	

	@InjectMocks
    private HotelReviewService hotelReviewService;

    @Mock
    private HotelReviewRepository reviewRepo;

    @Mock
    private ModelMapper modelMap;

    private HotelReview review;
    private HotelReviewDTO reviewDto;

    @BeforeEach
    void setUp() {
        review = new HotelReview("R001", "U001", "H001", "Abhay", 4.5, "Great stay!",
				"2025-10-25T10:00:00");
        
        

        reviewDto = new HotelReviewDTO( "Abhay", 4.5, "Great stay!",
				"2025-10-25T10:00:00");
    }

    @Test
    void testInsertHotelReviewById() throws Exception {
        when(reviewRepo.findById("R001")).thenReturn(Optional.empty());
        when(reviewRepo.save(review)).thenReturn(review);

        ResponseEntity<HotelReview> response = hotelReviewService.insertHotelReviewById(review);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(review, response.getBody());
    }

    @Test
    void testGetAllReviewByHotelId() throws Exception {
        List<HotelReview> reviewList = List.of(review);

        when(reviewRepo.findByHotelId("H001")).thenReturn(reviewList);
        when(modelMap.map(review, HotelReviewDTO.class)).thenReturn(reviewDto);

        ResponseEntity<List<HotelReviewDTO>> response = hotelReviewService.getAllReviewByHotelId("H001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(List.of(reviewDto) , response.getBody());
    }


}
