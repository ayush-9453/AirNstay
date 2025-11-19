import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class FlightServiceService {

  constructor(private flight : HttpClient) { }

  flightUrl = "http://localhost:8090/flight";

  getFlightById(id : String) : Observable<any> {
    return this.flight.get(this.flightUrl + "/getFlightById/" + id , {observe : 'response'} );
  }


  getSearchedFlights(data : any) : Observable<any> {
    let paramdata = new HttpParams();
    paramdata = paramdata.append('dep', data.departure);
    paramdata = paramdata.append('arr', data.arrival);
    paramdata = paramdata.append('date', data.departureDate);
    paramdata = paramdata.append('class', data.classType);

    return this.flight.get(this.flightUrl + "/getFlightSearchedData", { params: paramdata , observe : 'response' } );
  }

  


}
