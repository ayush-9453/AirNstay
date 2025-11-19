package com.cognizant.demo.service;

import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.PdfGenerationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    private final PdfService pdfService = new PdfService();

    private Booking validBooking() {
        FlightBooking booking = new FlightBooking();
        booking.setBookingId("BKGFT712353");
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(Booking.Status.confirmed);
        booking.setType(Booking.Type.flight);
        booking.setAirline("IndiGo");
        booking.setDeparture("Delhi");
        booking.setArrival("Goa");
        booking.setDepartureDate("2025-11-01");
        booking.setArrivalDate("2025-11-01");
        booking.setDuration("2h");
        booking.setClassType("Economy");

        ContactInfo contact = new ContactInfo(null, "IN", "9876543210", "anshulmarathe311@gmail.com", booking);
        booking.setContactInfo(contact);

        Payment payment = new Payment("PAYFL169975", "T418026", new BigDecimal("4586.56"), "Paid", "UPI", LocalDateTime.now(), booking);
        booking.setPayment(payment);

        Traveller traveller = new Traveller(null, "Anshul", "Marathe", 22, "Male", booking);
        booking.setTravellers(List.of(traveller));

        return booking;
    }

    @Test
    void createBookingPdf_Success() {
        Booking booking = validBooking();
        byte[] pdfBytes = pdfService.createBookingPdf(booking);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void createBookingPdf_MissingTravellers_ThrowsException() {
        Booking booking = validBooking();
        booking.setTravellers(null);

        PdfGenerationException ex = assertThrows(PdfGenerationException.class, () -> pdfService.createBookingPdf(booking));
        assertEquals("Traveller data is missing or empty.", ex.getMessage());
    }

    @Test
    void createBookingPdf_MissingContactEmail_ThrowsException() {
        Booking booking = validBooking();
        booking.getContactInfo().setEmail(null);

        PdfGenerationException ex = assertThrows(PdfGenerationException.class, () -> pdfService.createBookingPdf(booking));
        assertEquals("Contact email is missing.", ex.getMessage());
    }

    @Test
    void createBookingPdf_MissingBookingId_ThrowsException() {
        Booking booking = validBooking();
        booking.setBookingId(null);

        PdfGenerationException ex = assertThrows(PdfGenerationException.class, () -> pdfService.createBookingPdf(booking));
        assertEquals("Booking ID is missing.", ex.getMessage());
    }
}
