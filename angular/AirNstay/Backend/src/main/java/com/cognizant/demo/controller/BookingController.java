package com.cognizant.demo.controller;

import com.cognizant.demo.dto.BookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.FlightBooking;
import com.cognizant.demo.entity.HotelBooking;
import com.cognizant.demo.entity.PackageBooking;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.RecordAlreadyFoundException;
import com.cognizant.demo.service.PdfService;
import com.cognizant.demo.serviceImp.IBooking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {


    @Autowired
    private IBooking bookService;

    @Autowired
    private PdfService pdfService;

    // --- Create Bookings ---
    @PostMapping("/flight")
    public ResponseEntity<BookingDTO> createFlightBooking(@Valid @RequestBody FlightBooking flightbook) throws RecordAlreadyFoundException {
        log.info("Creating flight booking with ID: {}", flightbook.getBookingId());
        log.info("data to be stored is " , flightbook);
        return bookService.createFlightBooking(flightbook);
    }

    @PostMapping("/hotel")
    public ResponseEntity<BookingDTO> createHotelBooking(@Valid @RequestBody HotelBooking hotelbook) throws RecordAlreadyFoundException {
        log.info("Creating hotel booking with ID: {}", hotelbook.getBookingId());
        return bookService.createHotelBooking(hotelbook);
    }

    @PostMapping("/package")
    public ResponseEntity<BookingDTO> createPackageBooking(@Valid @RequestBody PackageBooking packagebook) throws RecordAlreadyFoundException {
        log.info("Creating package booking with ID: {}", packagebook.getBookingId());
        return bookService.createPackageBooking(packagebook);
    }

    // --- Get Booking by ID ---
    @GetMapping("/flight/{bookingId}")
    public ResponseEntity<BookingDTO> getFlightBookingById(@PathVariable String bookingId) throws NoDataFoundException {
        log.info("Fetching flight booking with ID: {}", bookingId);
        return bookService.getFlightBookingById(bookingId);
    }

    @GetMapping("/hotel/{bookingId}")
    public ResponseEntity<BookingDTO> getHotelBookingById(@PathVariable String bookingId) throws NoDataFoundException {
        log.info("Fetching hotel booking with ID: {}", bookingId);
        return bookService.getHotelBookingById(bookingId);
    }

    @GetMapping("/package/{bookingId}")
    public ResponseEntity<BookingDTO> getPackageBookingById(@PathVariable String bookingId) throws NoDataFoundException {
        log.info("Fetching package booking with ID: {}", bookingId);
        return bookService.getPackageBookingById(bookingId);
    }

    // --- Get All Bookings ---
    @GetMapping("/flight/all")
    public ResponseEntity<List<BookingDTO>> getAllFlightBookings() throws NoDataFoundException {
        log.info("Fetching all flight bookings");
        return bookService.getAllFlightBookings();
    }

    @GetMapping("/hotel/all")
    public ResponseEntity<List<BookingDTO>> getAllHotelBookings() throws NoDataFoundException {
        log.info("Fetching all hotel bookings");
        return bookService.getAllHotelBookings();
    }

    @GetMapping("/package/all")
    public ResponseEntity<List<BookingDTO>> getAllPackageBookings() throws NoDataFoundException {
        log.info("Fetching all package bookings");
        return bookService.getAllPackageBookings();
    }

    // --- PDF Generation ---
    @GetMapping(value = "/pdf/{bookingId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateBookingPdf(@PathVariable String bookingId) throws NoDataFoundException {
        log.info("Generating PDF for booking ID: {}", bookingId);
        Booking booking = bookService.getBookingEntityById(bookingId);
        byte[] pdf = pdfService.createBookingPdf(booking);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BookingConfirmation_" + bookingId + ".pdf")
                .body(pdf);
    }
}
