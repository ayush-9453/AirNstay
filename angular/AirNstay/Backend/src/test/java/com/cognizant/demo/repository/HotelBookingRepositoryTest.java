package com.cognizant.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.HotelBooking;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class HotelBookingRepositoryTest {

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    @BeforeEach
    void cleanDatabase() {
        hotelBookingRepository.deleteAll();
    }

    @Test
    void testFindByBookingId() {
        HotelBooking booking1 = new HotelBooking();
        booking1.setBookingId("H1001");
        booking1.setUserId("U1001");
        booking1.setName("Taj Hotel");
        booking1.setBookingDate(LocalDateTime.now()); // Required field


        hotelBookingRepository.save(booking1);

        List<HotelBooking> result = hotelBookingRepository.findByBookingId("H1001");

        assertThat(result).hasSize(1);
        assertThat(result).extracting(HotelBooking::getName).contains("Taj Hotel");
    }

   
}