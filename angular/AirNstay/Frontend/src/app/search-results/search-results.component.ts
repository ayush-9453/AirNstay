import { Component } from '@angular/core';
import { FlightServiceService } from '../flight-service.service';
import FlightDetails from '../flight-details';
import { ActivatedRoute, Router } from '@angular/router';
import { TemopraryStoreService } from '../temoprary-store.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-search-results',
  standalone: false,
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css'
})
export class SearchResultsComponent {


  constructor(private flightserviceObj: FlightServiceService, private route: ActivatedRoute, private router: Router , private temporaryDetailStoreObj: TemopraryStoreService) { }

  flightLst!: FlightDetails[];
  lengthOfFlightList! : number;
  

  // getting the flight details from the backend storage db.json
  ngOnInit() {
   

    this.route.queryParamMap.subscribe(params => {
      let departure = params.get('departure');
      let arrival = params.get('arrival');
      let classType = params.get('classType');
      let departureDate = params.get('departureDate');

      let formattedInputDate : string | undefined ;
      if (departureDate) {
        const date = departureDate.split('-');
        
        formattedInputDate = `${date[2]}/${date[1]}/${date[0]}`; // dd/mm/yyyy
        
      } 
      
      const data = {
        departure : departure,
        arrival : arrival,
        departureDate : formattedInputDate,
        classType : classType
      };
       
      console.log("Search Data: ", data);

      
      this.flightserviceObj.getSearchedFlights(data).subscribe({
        next: (response : HttpResponse<any>) => { 
              console.log("Response Obtained :" + JSON.stringify(response));
              if(response.status == 200){
                this.flightLst = response.body;
                
              }
              else
                console.log("Not able to fetch the data from the backend");
        },
        // error: (err) => {console.log("Error while fetching the data: " + JSON.stringify(err))},
        complete: () => console.log("Flight Successfully Fetched")
      })

    });

    let data = this.temporaryDetailStoreObj.getFlightSearchDetails();
     console.log("Flight total amount set to: ", data);
  }

  
  

  backToSearch() {

    // clearing the data the had been stored temporarily in the service
    this.temporaryDetailStoreObj.clearFlightSearchDetails();

    // routing it back to the search flight page
    this.router.navigate(['flights']);
  }

  goToPayment(flightId : string){
    this.router.navigate(['flight-Payment'] ,
                        {queryParams : {flightId}});
  }


}
