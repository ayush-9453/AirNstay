package com.cognizant.demo.service;

import com.cognizant.demo.dto.BookingDTO;
import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.RecordAlreadyFoundException;
import com.cognizant.demo.repository.BookingRepository;
import com.cognizant.demo.repository.FlightBookingRepository;
import com.cognizant.demo.repository.HotelBookingRepository;
import com.cognizant.demo.repository.PackageBookingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepo;
    @Mock private FlightBookingRepository flightRepo;
    @Mock private HotelBookingRepository hotelRepo;
    @Mock private PackageBookingRepository packageRepo;
    @Mock private ModelMapper modelMapper;
    @Mock private PdfService pdfService;
    @Mock private EmailService emailService;

    @InjectMocks private BookingService bookingService;

    private FlightBooking flight;
    private HotelBooking hotel;
    private PackageBooking pkg;
    private BookingDTO flightDto;
    private BookingDTO hotelDto;
    private BookingDTO packageDto;

    @BeforeEach
    void setup() {
        flight = new FlightBooking();
        flight.setBookingId("BKGFT712353");
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
        flight.setContactInfo(new ContactInfo(null, "IN", "9876543210", "test@example.com", flight));
        flight.setPayment(new Payment("PAYFL169975", "T418026", new BigDecimal("4500"), "Paid", "UPI", LocalDateTime.now(), flight));
        flight.setTravellers(List.of(new Traveller(null, "Anshul", "Marathe", 22, "Male", flight)));

        flightDto = new BookingDTO();
        flightDto.setBookingId("BKGFT712353");
        flightDto.setUserId("T418026");
        flightDto.setBookingDate(flight.getBookingDate());
        flightDto.setStatus(BookingDTO.BookingStatus.confirmed);
        flightDto.setType(BookingDTO.BookingType.flight);

        hotel = new HotelBooking();
        hotel.setBookingId("BKGHT998877");
        hotel.setUserId("T418027");
        hotel.setType(Booking.Type.hotel);
        hotel.setStatus(Booking.Status.confirmed);
        hotel.setBookingDate(LocalDateTime.now());
        hotel.setName("Taj Palace");
        hotel.setCheckInDate("2025-12-10");
        hotel.setCheckOutDate("2025-12-15");
        hotel.setContactInfo(new ContactInfo(null, "IN", "9123456789", "hotel@example.com", hotel));
        hotel.setPayment(new Payment("PAYHT998877", "T418027", new BigDecimal("12000"), "Paid", "Card", LocalDateTime.now(), hotel));
        hotel.setTravellers(List.of(new Traveller(null, "Anuj", "Sharma", 28, "Male", hotel)));

        hotelDto = new BookingDTO();
        hotelDto.setBookingId("BKGHT998877");
        hotelDto.setUserId("T418027");
        hotelDto.setBookingDate(hotel.getBookingDate());
        hotelDto.setStatus(BookingDTO.BookingStatus.confirmed);
        hotelDto.setType(BookingDTO.BookingType.hotel);

        pkg = new PackageBooking();
        pkg.setBookingId("BKGPKG556644");
        pkg.setUserId("T418028");
        pkg.setType(Booking.Type.packages);
        pkg.setStatus(Booking.Status.confirmed);
        pkg.setBookingDate(LocalDateTime.now());
        pkg.setPackageTitle("Goa Explorer");
        pkg.setContactInfo(new ContactInfo(null, "IN", "9988776655", "package@example.com", pkg));
        pkg.setPayment(new Payment("PAYPKG556644", "T418028", new BigDecimal("18000"), "Paid", "Card", LocalDateTime.now(), pkg));
        pkg.setTravellers(List.of(new Traveller(null, "Karan", "Mehta", 23, "Male", pkg)));

        packageDto = new BookingDTO();
        packageDto.setBookingId("BKGPKG556644");
        packageDto.setUserId("T418028");
        packageDto.setBookingDate(pkg.getBookingDate());
        packageDto.setStatus(BookingDTO.BookingStatus.confirmed);
        packageDto.setType(BookingDTO.BookingType.packages);
    }

    @Test
    void createFlightBooking_Success() throws Exception {
        when(bookingRepo.findById("BKGFT712353")).thenReturn(Optional.empty());
        when(bookingRepo.save(flight)).thenReturn(flight);
        when(modelMapper.map(flight, BookingDTO.class)).thenReturn(flightDto);

        ResponseEntity<BookingDTO> response = bookingService.createFlightBooking(flight);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("BKGFT712353", response.getBody().getBookingId());
        verify(emailService).sendEmailWithAttachment(any(), any(), any(), any(), any());
    }

    @Test
    void createHotelBooking_Success() throws Exception {
        when(bookingRepo.findById("BKGHT998877")).thenReturn(Optional.empty());
        when(bookingRepo.save(hotel)).thenReturn(hotel);
        when(modelMapper.map(hotel, BookingDTO.class)).thenReturn(hotelDto);

        ResponseEntity<BookingDTO> response = bookingService.createHotelBooking(hotel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("BKGHT998877", response.getBody().getBookingId());
        verify(emailService).sendEmailWithAttachment(any(), any(), any(), any(), any());
    }

    @Test
    void createPackageBooking_Success() throws Exception {
        when(bookingRepo.findById("BKGPKG556644")).thenReturn(Optional.empty());
        when(bookingRepo.save(pkg)).thenReturn(pkg);
        when(modelMapper.map(pkg, BookingDTO.class)).thenReturn(packageDto);

        ResponseEntity<BookingDTO> response = bookingService.createPackageBooking(pkg);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("BKGPKG556644", response.getBody().getBookingId());
        verify(emailService).sendEmailWithAttachment(any(), any(), any(), any(), any());
    }

    @Test
    void getFlightBookingById_Success() throws Exception {
        when(flightRepo.findById("BKGFT712353")).thenReturn(Optional.of(flight));
        when(modelMapper.map(flight, BookingDTO.class)).thenReturn(flightDto);

        ResponseEntity<BookingDTO> response = bookingService.getFlightBookingById("BKGFT712353");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BKGFT712353", response.getBody().getBookingId());
    }

    @Test
    void getHotelBookingById_Success() throws Exception {
        when(hotelRepo.findById("BKGHT998877")).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotel, BookingDTO.class)).thenReturn(hotelDto);

        ResponseEntity<BookingDTO> response = bookingService.getHotelBookingById("BKGHT998877");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BKGHT998877", response.getBody().getBookingId());
    }

    @Test
    void getPackageBookingById_Success() throws Exception {
        when(packageRepo.findById("BKGPKG556644")).thenReturn(Optional.of(pkg));
        when(modelMapper.map(pkg, BookingDTO.class)).thenReturn(packageDto);

        ResponseEntity<BookingDTO> response = bookingService.getPackageBookingById("BKGPKG556644");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BKGPKG556644", response.getBody().getBookingId());
    }

    @Test
    void getBookingEntityById_Success() throws Exception {
        when(bookingRepo.findById("BKGFT712353")).thenReturn(Optional.of(flight));

        Booking result = bookingService.getBookingEntityById("BKGFT712353");

        assertNotNull(result);
        assertEquals("BKGFT712353", result.getBookingId());
    }

    @Test
    void getBookingEntityById_NotFound() {
        when(bookingRepo.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(NoDataFoundException.class, () -> bookingService.getBookingEntityById("UNKNOWN"));
    }

    @Test
    void getAllFlightBookings_Success() throws Exception {
        when(flightRepo.findAll()).thenReturn(List.of(flight));
        when(modelMapper.map(flight, BookingDTO.class)).thenReturn(flightDto);

        ResponseEntity<List<BookingDTO>> response = bookingService.getAllFlightBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("BKGFT712353", response.getBody().get(0).getBookingId());
    }

    @Test
    void getAllFlightBookings_Empty() {
        when(flightRepo.findAll()).thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () -> bookingService.getAllFlightBookings());
    }

    @Test
    void getAllHotelBookings_Success() throws Exception {
        when(hotelRepo.findAll()).thenReturn(List.of(hotel));
        when(modelMapper.map(hotel, BookingDTO.class)).thenReturn(hotelDto);

        ResponseEntity<List<BookingDTO>> response = bookingService.getAllHotelBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("BKGHT998877", response.getBody().get(0).getBookingId());
    }

    @Test
    void getAllHotelBookings_Empty() {
        when(hotelRepo.findAll()).thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () -> bookingService.getAllHotelBookings());
    }

    @Test
    void getAllPackageBookings_Success() throws Exception {
        when(packageRepo.findAll()).thenReturn(List.of(pkg));
        when(modelMapper.map(pkg, BookingDTO.class)).thenReturn(packageDto);

        ResponseEntity<List<BookingDTO>> response = bookingService.getAllPackageBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("BKGPKG556644", response.getBody().get(0).getBookingId());
    }

    @Test
    void getAllPackageBookings_Empty() {
        when(packageRepo.findAll()).thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () -> bookingService.getAllPackageBookings());
    }
}
