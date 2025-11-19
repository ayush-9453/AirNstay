import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import hotelBookingDetails from './hotelBooking';
import flightBookingDetails from './flightBooking';
import PackageBookingDetails from './packagebookingDetails';

@Injectable({
  providedIn: 'root'
})
export class GetBookingService {

  constructor(private bookingService : HttpClient) { }

  url = "http://localhost:8090/booking";

  getBookedFlights(userId : string | null) : Observable<any> {
    return  this.bookingService.get(this.url + "/getBookedFlightByUserId/" + userId , { observe: 'response' });
  }


  getBookedHotels(userId : string | null) : Observable<any> {
    return  this.bookingService.get(this.url + "/getBookedHotelByUserId/" + userId , { observe: 'response' });
  }


  getBookedPackage(userId : string | null) : Observable<any>{
    return this.bookingService.get(this.url + "/getBookedPackageByUserId/" + userId , {observe : 'response'});
  }



  cancelFlightBooking(bookingId : string) :Observable<any> { 
    return this.bookingService.post(this.url + "/cancelBookedFlight/" + bookingId , null, { observe: 'response', responseType: 'text' });

  }

  cancelHotelBooking(bookingId : string) :Observable<any> { 
    return this.bookingService.post(this.url + "/cancelBookedHotel/" + bookingId ,null , { observe: 'response' ,responseType: 'text'});
  }

  cancelPackageBooking(bookingId : string) : Observable<any>{
    return this.bookingService.post(this.url + "/cancelBookedPackage/" + bookingId ,null ,  {observe: 'response' ,responseType: 'text'})
  }


  
}
