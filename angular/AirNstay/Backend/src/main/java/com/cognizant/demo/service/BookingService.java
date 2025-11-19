package com.cognizant.demo.service;

import com.cognizant.demo.dto.BookingDTO;
import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.RecordAlreadyFoundException;
import com.cognizant.demo.exception.PdfGenerationException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.FlightBookingRepository;
import com.cognizant.demo.repository.HotelBookingRepository;
import com.cognizant.demo.repository.PackageBookingRepository;
import com.cognizant.demo.serviceImp.IBooking;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingService implements IBooking {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightBookingRepository flightrepo;

    @Autowired
    private HotelBookingRepository hotelrepo;

    @Autowired
    private PackageBookingRepository packagerepo;

    @Autowired
    private ModelMapper model;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    // --- Create Bookings ---
    
    public ResponseEntity<BookingDTO> createFlightBooking(FlightBooking flightbook) throws RecordAlreadyFoundException {
        Optional<Booking> existing = bookingRepository.findById(flightbook.getBookingId());
        if (existing.isPresent()) {
            throw new RecordAlreadyFoundException("Flight booking with ID " + flightbook.getBookingId() + " already exists.");
        }
        
        setBookingReferences(flightbook);
        FlightBooking saved = bookingRepository.save(flightbook);
        sendConfirmationEmail(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(model.map(saved, BookingDTO.class));
    }

    
    public ResponseEntity<BookingDTO> createHotelBooking(HotelBooking hotelbook) throws RecordAlreadyFoundException {
        Optional<Booking> existing = bookingRepository.findById(hotelbook.getBookingId());
        if (existing.isPresent()) {
            throw new RecordAlreadyFoundException("Hotel booking with ID " + hotelbook.getBookingId() + " already exists.");
        }

        setBookingReferences(hotelbook);
        HotelBooking saved = bookingRepository.save(hotelbook);
        sendConfirmationEmail(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(model.map(saved, BookingDTO.class));
    }

    
    public ResponseEntity<BookingDTO> createPackageBooking(PackageBooking packagebook) throws RecordAlreadyFoundException {
        Optional<Booking> existing = bookingRepository.findById(packagebook.getBookingId());
        if (existing.isPresent()) {
            throw new RecordAlreadyFoundException("Package booking with ID " + packagebook.getBookingId() + " already exists.");
        }

        setBookingReferences(packagebook);
        PackageBooking saved = bookingRepository.save(packagebook);
        sendConfirmationEmail(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(model.map(saved, BookingDTO.class));
    }

    // --- Get Booking by ID ---
    
    public ResponseEntity<BookingDTO> getFlightBookingById(String bookingId) throws NoDataFoundException {
        Optional<FlightBooking> booking = flightrepo.findById(bookingId);
        FlightBooking found = booking.orElseThrow(() ->
                new NoDataFoundException("Flight booking with ID " + bookingId + " not found."));
        return ResponseEntity.ok(model.map(found, BookingDTO.class));
    }

    
    public ResponseEntity<BookingDTO> getHotelBookingById(String bookingId) throws NoDataFoundException {
        Optional<HotelBooking> booking = hotelrepo.findById(bookingId);
        HotelBooking found = booking.orElseThrow(() ->
                new NoDataFoundException("Hotel booking with ID " + bookingId + " not found."));
        return ResponseEntity.ok(model.map(found, BookingDTO.class));
    }

    
    public ResponseEntity<BookingDTO> getPackageBookingById(String bookingId) throws NoDataFoundException {
        Optional<PackageBooking> booking = packagerepo.findById(bookingId);
        PackageBooking found = booking.orElseThrow(() ->
                new NoDataFoundException("Package booking with ID " + bookingId + " not found."));
        return ResponseEntity.ok(model.map(found, BookingDTO.class));
    }

    // --- Get All Bookings ---
    
    public ResponseEntity<List<BookingDTO>> getAllFlightBookings() throws NoDataFoundException {
        List<FlightBooking> bookings = flightrepo.findAll();
        if (bookings.isEmpty()) {
            throw new NoDataFoundException("No flight bookings found.");
        }
        List<BookingDTO> dtoList = bookings.stream().map(b -> model.map(b, BookingDTO.class)).toList();
        return ResponseEntity.ok(dtoList);
    }

    
    public ResponseEntity<List<BookingDTO>> getAllHotelBookings() throws NoDataFoundException {
        List<HotelBooking> bookings = hotelrepo.findAll();
        if (bookings.isEmpty()) {
            throw new NoDataFoundException("No hotel bookings found.");
        }
        List<BookingDTO> dtoList = bookings.stream().map(b -> model.map(b, BookingDTO.class)).toList();
        return ResponseEntity.ok(dtoList);
    }

    
    public ResponseEntity<List<BookingDTO>> getAllPackageBookings() throws NoDataFoundException {
        List<PackageBooking> bookings = packagerepo.findAll();
        if (bookings.isEmpty()) {
            throw new NoDataFoundException("No package bookings found.");
        }
        List<BookingDTO> dtoList = bookings.stream().map(b -> model.map(b, BookingDTO.class)).toList();
        return ResponseEntity.ok(dtoList);
    }

    // --- PDF Support ---
    
    public Booking getBookingEntityById(String bookingId) throws NoDataFoundException {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElseThrow(() ->
                new NoDataFoundException("Booking with ID " + bookingId + " not found."));
    }

    // --- Email Helper ---
    public void sendConfirmationEmail(Booking booking) {
        try {
            if (booking.getContactInfo() == null || booking.getContactInfo().getEmail() == null) {
                log.warn("Cannot send email: missing contact info for booking {}", booking.getBookingId());
                return;
            }

            String toEmail = booking.getContactInfo().getEmail();
            String customerName = booking.getTravellers() != null && !booking.getTravellers().isEmpty()
                    ? booking.getTravellers().get(0).getFirstName()
                    : "Valued Customer";

            byte[] pdfBytes = pdfService.createBookingPdf(booking);
            String subject = "Your Booking Confirmation - ID: " + booking.getBookingId();
            String body = "Dear " + customerName + ",\n\nPlease find your booking confirmation PDF attached.\n\nThank you for booking with us!";
            String attachmentName = "BookingConfirmation_" + booking.getBookingId() + ".pdf";

            emailService.sendEmailWithAttachment(toEmail, subject, body, pdfBytes, attachmentName);
            log.info("Email sent for booking {}", booking.getBookingId());

        } catch (PdfGenerationException e) {
            log.warn("PDF/Email failed for booking {}: {}", booking.getBookingId(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during email for booking {}", booking.getBookingId(), e);
        }
    }

    // --- Reference Setter ---
    private void setBookingReferences(Booking booking) {
        Optional.ofNullable(booking.getContactInfo()).ifPresent(c -> c.setBooking(booking));
        Optional.ofNullable(booking.getPayment()).ifPresent(p -> p.setBooking(booking));
        Optional.ofNullable(booking.getTravellers()).ifPresent(list ->
                list.forEach(t -> t.setBooking(booking)));
    }
}
