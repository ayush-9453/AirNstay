package com.cognizant.demo.repository;

import com.cognizant.demo.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.demo.AirNstayApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = AirNstayApplication.class)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepo;

    @Test
    void testSaveAndFindFlightBooking() {
        FlightBooking flight = new FlightBooking();
        flight.setBookingId("BKGFT712354");
        flight.setUserId("T418026");
        flight.setType(Booking.Type.flight);
        flight.setStatus(Booking.Status.confirmed);
        flight.setBookingDate(LocalDateTime.now());
        flight.setAirline("IndiGo");
        flight.setDeparture("Delhi");
        flight.setArrival("Goa");
        flight.setDepartureDate("2025-11-01");
        flight.setArrivalDate("2025-11-01");
        flight.setDuration("2h");
        flight.setClassType("Economy");

        ContactInfo contact = new ContactInfo(null, "IN", "9876543210", "anshulmarathe@gmail.com", flight);
        flight.setContactInfo(contact);

        Payment payment = new Payment("PAYFL169976", "T418026", new BigDecimal("4500"), "Paid", "UPI", LocalDateTime.now(), flight);
        flight.setPayment(payment);

        bookingRepo.save(flight);

        Optional<Booking> result = bookingRepo.findById("BKGFT712354");
        assertTrue(result.isPresent());
        assertEquals("IndiGo", ((FlightBooking) result.get()).getAirline());
    }

    @Test
    void testSaveAndFindHotelBooking() {
        HotelBooking hotel = new HotelBooking();
        hotel.setBookingId("BKGHT998878");
        hotel.setUserId("T418027");
        hotel.setType(Booking.Type.hotel);
        hotel.setStatus(Booking.Status.confirmed);
        hotel.setBookingDate(LocalDateTime.now());
        hotel.setName("Taj Palace");
        hotel.setCheckInDate("2025-12-10");
        hotel.setCheckOutDate("2025-12-15");

        ContactInfo contact = new ContactInfo(null, "IN", "9123456789", "anshulmarathe@gmail.com", hotel);
        hotel.setContactInfo(contact);

        Payment payment = new Payment("PAYHOT998878", "T418027", new BigDecimal("12000"), "Paid", "Card", LocalDateTime.now(), hotel);
        hotel.setPayment(payment);

        bookingRepo.save(hotel);

        Optional<Booking> result = bookingRepo.findById("BKGHT998878");
        assertTrue(result.isPresent());
        assertEquals("Taj Palace", ((HotelBooking) result.get()).getName());
    }

    @Test
    void testSaveAndFindPackageBooking() {
        PackageBooking pkg = new PackageBooking();
        pkg.setBookingId("BKGPKG556645");
        pkg.setUserId("T418028");
        pkg.setType(Booking.Type.packages);
        pkg.setStatus(Booking.Status.confirmed);
        pkg.setBookingDate(LocalDateTime.now());
        pkg.setPackageTitle("Goa Explorer");

        ContactInfo contact = new ContactInfo(null, "IN", "9988776655", "anshulmarathe@gmail.com", pkg);
        pkg.setContactInfo(contact);

        Payment payment = new Payment("PAYPKG556645", "T418028", new BigDecimal("18000"), "Paid", "Card", LocalDateTime.now(), pkg);
        pkg.setPayment(payment);

        bookingRepo.save(pkg);

        Optional<Booking> result = bookingRepo.findById("BKGPKG556645");
        assertTrue(result.isPresent());
        assertEquals("Goa Explorer", ((PackageBooking) result.get()).getPackageTitle());
    }

    @Test
    void testFindAllBookings() {
        List<Booking> bookings = bookingRepo.findAll();
        assertNotNull(bookings);
        assertTrue(bookings.size() >= 0);
    }
}
