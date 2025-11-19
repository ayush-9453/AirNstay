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

import com.cognizant.demo.dto.HotelBookingDTO;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.HotelBookingService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class HotelBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelBookingService hotelBookingService;

    
    @Test
    void testGetBookedHotelByUserIdSuccess() throws Exception {
    	HotelBookingDTO booking = new HotelBookingDTO();
    	booking.setBookingId("H1001");
    	booking.setUserId("U1001");
        booking.setName("Grand Palace");
    
        
        Mockito.when(hotelBookingService.getBookedHotelByUserId("U1001"))
        .thenReturn(ResponseEntity.ok(List.of(booking)));

        mockMvc.perform(get("/booking/getBookedHotelByUserId/U1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value("H1001"))
                .andExpect(jsonPath("$[0].name").value("Grand Palace"));
    }

    
    @Test
    void testCancelBookedHotelSuccess() throws Exception {
        Mockito.when(hotelBookingService.cancelBookedHotel("H1001"))
               .thenReturn(ResponseEntity.ok("Hotel booking cancelled successfully"));

        mockMvc.perform(post("/booking/cancelBookedHotel/H1001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hotel booking cancelled successfully"));
    }

}