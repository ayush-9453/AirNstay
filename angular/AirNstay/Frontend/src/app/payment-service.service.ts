import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private rest: HttpClient) { }

  strUrl:string="http://localhost:8090/booking";

 submitFlightBooking(bookingData: any):Observable<any> {
  return this.rest.post(this.strUrl+"/flight",bookingData);
}

submitHotelBooking(bookingData: any):Observable<any> {
  return this.rest.post(this.strUrl+"/hotel", bookingData);
}

 
submitPackageBooking(bookingData: any):Observable<any> {
  return this.rest.post(this.strUrl+"/package", bookingData);
}

}