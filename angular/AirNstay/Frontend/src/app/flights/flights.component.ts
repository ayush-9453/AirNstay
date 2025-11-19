import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TemopraryStoreService } from '../temoprary-store.service';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-flights',
  standalone: false,
  templateUrl: './flights.component.html',
  styleUrl: './flights.component.css'
})
export class FlightsComponent {
  currency: any;
  searchDetailsForm!: FormGroup;
  defaultDate! : string;
  passengerType! : string
  showAlert : boolean = false;

  constructor(private route: Router, private searchDetails: FormBuilder , private temporaryDetailStoreObj: TemopraryStoreService , private alertObj : ToastService) {

    this.defaultDate = new Date().toISOString().slice(0, 10);
    

    this.searchDetailsForm = this.searchDetails.group({
      departure: ['', [Validators.required , Validators.pattern(/^[A-Za-z\s]+$/)]],
      arrival: ['', [Validators.required , Validators.pattern(/^[A-Za-z\s]+$/)]],
      departureDate: [this.defaultDate, Validators.required],
      classType: ['', Validators.required],
      passengerType : ['Regular' , Validators.required],
    })
  }

  searchForFlight() {
    let departure = this.searchDetailsForm.get(['departure'])?.value;
    let arrival = this.searchDetailsForm.get(['arrival'])?.value;
    let departureDate = this.searchDetailsForm.get(['departureDate'])?.value;
    let classType = this.searchDetailsForm.get(['classType'])?.value;
    
    if (!departure || !arrival || !departureDate || !classType) {
      this.alertObj.showError("All fields are required!");
    } 
    else {
      this.route.navigate(['flights/search'],
        { queryParams: { departure,  arrival, departureDate, classType } }

      );
    }
    let passengerType = this.searchDetailsForm.get(['passengerType'])?.value;
    this.temporaryDetailStoreObj.storeFlightSearchDetails(passengerType);
    
  }

 



}
