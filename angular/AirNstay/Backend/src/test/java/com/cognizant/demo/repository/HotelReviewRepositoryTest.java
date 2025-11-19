package com.cognizant.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.dto.HotelReviewDTO;
import com.cognizant.demo.entity.HotelReview;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class HotelReviewRepositoryTest {

	@Autowired
	private HotelReviewRepository reviewRepo;

	private HotelReview review1;
	private HotelReview review2;

	@BeforeEach
    void setUp() {
        review1 = new HotelReview("R001", "U001", "H001", "Rohit", 4.5, "Great stay!",
				"2025-10-25T10:00:00");
        
        

        review2 = new HotelReview("R002", "U002", "H001", "Abhay", 4.5, "Great stay!",
				"2025-10-25T10:00:00");
        

        reviewRepo.save(review1);
        reviewRepo.save(review2);

    }
	
	@Test
	public void testFindByHotelId_Success() throws Exception {
		List<HotelReview> reviews = reviewRepo.findByHotelId("H001");

		assertEquals(2, reviews.size());
		assertTrue(reviews.stream().anyMatch(r -> r.getReviewId().equals("R001")));
       	assertTrue(reviews.stream().anyMatch(r -> r.getReviewId().equals("R002")));

	}

}
