package com.cognizant.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.cognizant.demo.AirNstayApplication;
import com.cognizant.demo.entity.FlightBooking;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
public class FlightBookingRepositoryTest {

    @Autowired
    private FlightBookingRepository flightBookingRepository;

    @Test
    void testFindByBookingId() {
    	
    	
        FlightBooking flightBooking1 = new FlightBooking();
        flightBooking1.setBookingId("B1001");
        flightBooking1.setUserId("U1001");
        flightBooking1.setFlightId("F123");
        flightBooking1.setAirline("AirlineX");
        flightBooking1.setBookingDate(LocalDateTime.now());


        flightBookingRepository.save(flightBooking1);
        
        

        List<FlightBooking> result = flightBookingRepository.findByBookingId("B1001");

        assertThat(result).hasSize(1);
        assertThat(result).extracting(FlightBooking::getFlightId).contains("F123");
    }

}