package com.cognizant.demo.serviceImp;

import java.util.List;
import org.springframework.http.ResponseEntity;

import com.cognizant.demo.dto.BookingDTO;
import com.cognizant.demo.entity.Booking;
import com.cognizant.demo.entity.FlightBooking;
import com.cognizant.demo.entity.HotelBooking;
import com.cognizant.demo.entity.PackageBooking;
import com.cognizant.demo.exception.NoDataFoundException;
import com.cognizant.demo.exception.RecordAlreadyFoundException;

public interface IBooking {
	ResponseEntity<BookingDTO> createFlightBooking(FlightBooking flightbook) throws RecordAlreadyFoundException;
    ResponseEntity<BookingDTO> createHotelBooking(HotelBooking hotebook) throws RecordAlreadyFoundException;
    ResponseEntity<BookingDTO> createPackageBooking(PackageBooking packagebook) throws RecordAlreadyFoundException;
    
    ResponseEntity<BookingDTO> getFlightBookingById(String id) throws NoDataFoundException;
    ResponseEntity<BookingDTO> getHotelBookingById(String id) throws NoDataFoundException;
    ResponseEntity<BookingDTO> getPackageBookingById(String id) throws NoDataFoundException;
    
    ResponseEntity<List<BookingDTO>> getAllFlightBookings() throws NoDataFoundException;
    ResponseEntity<List<BookingDTO>> getAllHotelBookings() throws NoDataFoundException;
    ResponseEntity<List<BookingDTO>> getAllPackageBookings() throws NoDataFoundException;
    
    Booking getBookingEntityById(String bookingId) throws NoDataFoundException;

}

