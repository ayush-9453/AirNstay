package com.cognizant.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognizant.demo.dto.FlightBookingDTO;
import com.cognizant.demo.service.FlightBookingService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FlightBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightBookingService flightBookingService;

    @Test
    void testGetBookedFlightByUserIdSuccess() throws Exception {
        FlightBookingDTO booking = new FlightBookingDTO();
        booking.setBookingId("B1001");
        booking.setFlightId("F123");
        booking.setAirline("AirlineX");
        booking.setDeparture("NYC");
        booking.setArrival("LAX");

        Mockito.when(flightBookingService.getBookedFlightByUserId("U1001"))
               .thenReturn(ResponseEntity.ok(List.of(booking)));

        mockMvc.perform(get("/booking/getBookedFlightByUserId/U1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value("B1001"))
                .andExpect(jsonPath("$[0].flightId").value("F123"));
    }

    @Test
    void testCancelBookedFlightSuccess() throws Exception {
        Mockito.when(flightBookingService.cancelBookedFlight("B1001"))
               .thenReturn(ResponseEntity.ok("Booking cancelled successfully"));

        mockMvc.perform(post("/booking/cancelBookedFlight/B1001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled successfully"));
    }
}