package com.cognizant.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.FlightBookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.Booking.Status;
import com.cognizant.demo.entity.FlightBooking;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.FlightBookingRepository;

@ExtendWith(MockitoExtension.class)
public class FlightBookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private FlightBookingRepository flightBookingRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightBookingService flightBookingService;

    private Booking booking;
    private FlightBooking flightBooking;
    private FlightBookingDTO flightBookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = new Booking();
        booking.setBookingId("B1001");
        booking.setUserId("U1001");
        booking.setStatus(Status.confirmed);

        flightBooking = new FlightBooking();
        flightBooking.setBookingId("B1001");
        flightBooking.setFlightId("F123");
        flightBooking.setAirline("AirlineX");

        flightBookingDTO = new FlightBookingDTO();
        flightBookingDTO.setFlightId("F123");
        flightBookingDTO.setAirline("AirlineX");
    }

    
    @Test
    void testGetBookedFlightByUserIdSuccess() throws NoDataFoundException {
        when(bookingRepo.findByUserId("U1001")).thenReturn(List.of(booking));
        when(flightBookingRepo.findByBookingId("B1001")).thenReturn(List.of(flightBooking));
        when(modelMapper.map(flightBooking, FlightBookingDTO.class)).thenReturn(flightBookingDTO);

        ResponseEntity<List<FlightBookingDTO>> response = flightBookingService.getBookedFlightByUserId("U1001");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getFlightId()).isEqualTo("F123");
    }

   
   
    @Test
    void testCancelBookedFlightSuccess() throws NoDataFoundException {
        when(bookingRepo.findById("B1001")).thenReturn(Optional.of(booking));

        ResponseEntity<String> response = flightBookingService.cancelBookedFlight("B1001");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Flight Cancelled Successfully");
        assertThat(booking.getStatus()).isEqualTo(Status.cancelled);
        verify(bookingRepo, times(1)).save(booking);
    }

}