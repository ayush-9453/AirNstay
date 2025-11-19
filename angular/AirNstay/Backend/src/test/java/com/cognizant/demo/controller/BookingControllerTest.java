package com.cognizant.demo.controller;

import com.cognizant.demo.dto.BookingDTO;
import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.service.PdfService;
import com.cognizant.demo.serviceImp.IBooking;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBooking bookingService;

    @MockBean
    private PdfService pdfService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO sampleBookingDTO(String id, BookingDTO.BookingType type) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(id);
        dto.setUserId("U001");
        dto.setBookingDate(LocalDateTime.now());
        dto.setStatus(BookingDTO.BookingStatus.confirmed);
        dto.setType(type);
        return dto;
    }

    private FlightBooking validFlightBooking() {
        FlightBooking flight = new FlightBooking();
        flight.setBookingId("BKGFT712353");
        flight.setUserId("U001");
        flight.setBookingDate(LocalDateTime.now());
        flight.setStatus(Booking.Status.confirmed);
        flight.setType(Booking.Type.flight);
        return flight;
    }

    private HotelBooking validHotelBooking() {
        HotelBooking hotel = new HotelBooking();
        hotel.setBookingId("BKGHOT998877");
        hotel.setUserId("U002");
        hotel.setBookingDate(LocalDateTime.now());
        hotel.setStatus(Booking.Status.confirmed);
        hotel.setType(Booking.Type.hotel);
        return hotel;
    }

    private PackageBooking validPackageBooking() {
        PackageBooking pkg = new PackageBooking();
        pkg.setBookingId("BKGPKG556644");
        pkg.setUserId("U003");
        pkg.setBookingDate(LocalDateTime.now());
        pkg.setStatus(Booking.Status.confirmed);
        pkg.setType(Booking.Type.packages);
        return pkg;
    }

    @Test
    void createFlightBooking_Success() throws Exception {
        when(bookingService.createFlightBooking(any()))
            .thenReturn(ResponseEntity.status(201).body(sampleBookingDTO("BKGFT712353", BookingDTO.BookingType.flight)));

        mockMvc.perform(post("/booking/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validFlightBooking())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.bookingId").value("BKGFT712353"));
    }

    @Test
    void createHotelBooking_Success() throws Exception {
        when(bookingService.createHotelBooking(any()))
            .thenReturn(ResponseEntity.status(201).body(sampleBookingDTO("BKGHOT998877", BookingDTO.BookingType.hotel)));

        mockMvc.perform(post("/booking/hotel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validHotelBooking())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.bookingId").value("BKGHOT998877"));
    }

    @Test
    void createPackageBooking_Success() throws Exception {
        when(bookingService.createPackageBooking(any()))
            .thenReturn(ResponseEntity.status(201).body(sampleBookingDTO("BKGPKG556644", BookingDTO.BookingType.packages)));

        mockMvc.perform(post("/booking/package")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPackageBooking())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.bookingId").value("BKGPKG556644"));
    }

    @Test
    void getFlightBookingById_Success() throws Exception {
        when(bookingService.getFlightBookingById("BKGFT712353"))
            .thenReturn(ResponseEntity.ok(sampleBookingDTO("BKGFT712353", BookingDTO.BookingType.flight)));

        mockMvc.perform(get("/booking/flight/BKGFT712353"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookingId").value("BKGFT712353"));
    }

    @Test
    void getHotelBookingById_Success() throws Exception {
        when(bookingService.getHotelBookingById("BKGHOT998877"))
            .thenReturn(ResponseEntity.ok(sampleBookingDTO("BKGHOT998877", BookingDTO.BookingType.hotel)));

        mockMvc.perform(get("/booking/hotel/BKGHOT998877"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookingId").value("BKGHOT998877"));
    }

    @Test
    void getPackageBookingById_Success() throws Exception {
        when(bookingService.getPackageBookingById("BKGPKG556644"))
            .thenReturn(ResponseEntity.ok(sampleBookingDTO("BKGPKG556644", BookingDTO.BookingType.packages)));

        mockMvc.perform(get("/booking/package/BKGPKG556644"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookingId").value("BKGPKG556644"));
    }

    @Test
    void getFlightBookingById_NotFound() throws Exception {
        when(bookingService.getFlightBookingById("BKGFT712353"))
            .thenThrow(new NoDataFoundException("Not found"));

        mockMvc.perform(get("/booking/flight/BKGFT712353"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllFlightBookings_Success() throws Exception {
        when(bookingService.getAllFlightBookings())
            .thenReturn(ResponseEntity.ok(List.of(sampleBookingDTO("BKGFT712353", BookingDTO.BookingType.flight))));

        mockMvc.perform(get("/booking/flight/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].bookingId").value("BKGFT712353"));
    }

    @Test
    void getAllHotelBookings_Success() throws Exception {
        when(bookingService.getAllHotelBookings())
            .thenReturn(ResponseEntity.ok(List.of(sampleBookingDTO("BKGHOT998877", BookingDTO.BookingType.hotel))));

        mockMvc.perform(get("/booking/hotel/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].bookingId").value("BKGHOT998877"));
    }

    @Test
    void getAllPackageBookings_Success() throws Exception {
        when(bookingService.getAllPackageBookings())
            .thenReturn(ResponseEntity.ok(List.of(sampleBookingDTO("BKGPKG556644", BookingDTO.BookingType.packages))));

        mockMvc.perform(get("/booking/package/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].bookingId").value("BKGPKG556644"));
    }

    @Test
    void getAllFlightBookings_NotFound() throws Exception {
        when(bookingService.getAllFlightBookings())
            .thenThrow(new NoDataFoundException("No bookings"));

        mockMvc.perform(get("/booking/flight/all"))
            .andExpect(status().isNotFound());
    }

    
    @Test
    void getAllHotelBookings_NotFound() throws Exception {
        when(bookingService.getAllHotelBookings())
            .thenThrow(new NoDataFoundException("No bookings"));

        mockMvc.perform(get("/booking/hotel/all"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllPackageBookings_NotFound() throws Exception {
        when(bookingService.getAllPackageBookings())
            .thenThrow(new NoDataFoundException("No bookings"));

        mockMvc.perform(get("/booking/package/all"))
            .andExpect(status().isNotFound());
    }

    @Test
    void generateBookingPdf_Success() throws Exception {
        Booking booking = new Booking();
        booking.setBookingId("BKGFT712353");
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(Booking.Status.confirmed);
        booking.setContactInfo(new ContactInfo(null, "IN", "1234567890", "test@example.com", booking));
        booking.setTravellers(List.of(new Traveller(null, "Anshul", "Marathe", 23, "Male", booking)));

        when(bookingService.getBookingEntityById("BKGFT712353")).thenReturn(booking);
        when(pdfService.createBookingPdf(any())).thenReturn("PDF".getBytes());

        mockMvc.perform(get("/booking/pdf/BKGFT712353"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=BookingConfirmation_BKGFT712353.pdf"));
    }
 
}
