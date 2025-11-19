package com.cognizant.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.HotelBookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.Booking.Status;
import com.cognizant.demo.entity.HotelBooking;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.HotelBookingRepository;

public class HotelBookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private HotelBookingRepository hotelBookingRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private HotelBookingService hotelBookingService;


    private Booking booking;
    private HotelBooking hotelBooking;
    private HotelBookingDTO hotelBookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        booking = new Booking();
        booking.setBookingId("H1001");
        booking.setUserId("U1001");
        booking.setStatus(Status.confirmed);

        hotelBooking = new HotelBooking();
        hotelBooking.setBookingId("H1001");
        hotelBooking.setName("Grand Palace");

        hotelBookingDTO = new HotelBookingDTO();
        hotelBookingDTO.setBookingId("H1001");
        hotelBookingDTO.setName("Grand Palace");
    }

    @Test
    void testGetBookedHotelByUserIdSuccess() throws NoDataFoundException {
        when(bookingRepo.findByUserId("U1001")).thenReturn(List.of(booking));
        when(hotelBookingRepo.findByBookingId("H1001")).thenReturn(List.of(hotelBooking));
        when(modelMapper.map(hotelBooking, HotelBookingDTO.class)).thenReturn(hotelBookingDTO);

        ResponseEntity<List<HotelBookingDTO>> response = hotelBookingService.getBookedHotelByUserId("U1001");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Grand Palace");
    }

 
    @Test
    void testCancelBookedHotelSuccess() throws NoDataFoundException {
        when(bookingRepo.findById("H1001")).thenReturn(Optional.of(booking));

        ResponseEntity<String> response = hotelBookingService.cancelBookedHotel("H1001");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hotel Cancelled Successfully");
        assertThat(booking.getStatus()).isEqualTo(Status.cancelled);
        verify(bookingRepo, times(1)).save(booking);
    }

  

}