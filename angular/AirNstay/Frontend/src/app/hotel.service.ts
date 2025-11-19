import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import AllReviews from './AllReviews';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  

  constructor( private hotel : HttpClient) { }

  hotelUrl = "http://localhost:8090/hotel";

  getAllHotel() : Observable<any>{
    return this.hotel.get(this.hotelUrl, { observe : 'response' } );
  }

  getHotelById(hotelId : string | null) : Observable<any>{
    return this.hotel.get(this.hotelUrl + "/details/" + hotelId , { observe : 'response' } );
  }

  getHotelByCity(city : string) : Observable<any>{
    return this.hotel.get(this.hotelUrl + "/results/" + city , { observe : 'response' } );
  }

  AddReviewByHotelId(newReview: AllReviews) : Observable<any> {
    return this.hotel.post(this.hotelUrl + "/insertHotelReview" , newReview , { observe : 'response' } );
  }

  getReviewByHotelID(hotelId : string | null) : Observable<any>{
    return this.hotel.get(this.hotelUrl + "/getAllReviewByHotelId/" + hotelId , { observe : 'response' } );
  }
 
}
