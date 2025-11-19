import { Component } from '@angular/core';
import { FormBuilder, FormGroup, MaxValidator, Validators } from '@angular/forms';
import { HotelService } from '../hotel.service';
import HotelDetails from '../hotel-details';
import { Router } from '@angular/router';
import { TemopraryStoreService } from '../temoprary-store.service';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';


@Component({
  selector: 'app-hotel',
  standalone: false,
  templateUrl: './hotel.component.html',
  styleUrl: './hotel.component.css'
})
export class HotelComponent {

  searchHotelsForm!: FormGroup;
  hotelLst!: any[];
  length!: number;
  checkIn!: string;
  checkOut!: string;
  showAlert: boolean = false;

  constructor(private searchDetails: FormBuilder, private hotelServiceObj: HotelService,
    private route: Router, private temporaryDetailStoreObj: TemopraryStoreService,
    private alertObj : ToastService) {

    this.checkIn = new Date().toISOString().split('T')[0];
    this.checkOut = new Date(Date.now() + 86400000).toISOString().split('T')[0];


    // validation of the hotel search form is done here
    this.searchHotelsForm = this.searchDetails.group({
      city: ['', [Validators.required , Validators.pattern(/^[A-Za-z\s]+$/)]],
      checkIn: [this.checkIn, Validators.required],
      checkOut: [this.checkOut, Validators.required],
      travellers: ['', [Validators.required ,Validators.min(1)]],
    })
  }

  searchHotels() {

    // getting the details from the form filter the hotel as per location
    let city = this.searchHotelsForm.get(['city'])?.value;
    let checkIn = this.searchHotelsForm.get(['checkIn'])?.value;
    let checkOut = this.searchHotelsForm.get(['checkOut'])?.value;
    let travellers = this.searchHotelsForm.get(['travellers'])?.value;

    if (city && checkIn && checkOut && travellers) {
      this.hotelServiceObj.getHotelByCity(city).subscribe({
      next: (response : HttpResponse<any>) => {
        console.log("Response Obtained :" + JSON.stringify(response)); 
        if(response.status == 200){
          this.hotelLst = response.body;
          this.length = this.hotelLst.length;
          }
        else
          console.log("Not able to fetch the data from the backend");
        },
      error: (err) => {console.log("Error while fetching the data: " + JSON.stringify(err)) , this.length = 0},
      complete: () => console.log("All Hotels Are Fetched Successfully"),
    })
    }
    else {
      this.alertObj.showError("All fields are required !");
    }  
  }



  sendToBooking(hotelId: string) {

    // store the details in a varibale 
    const storeHotel = {
      travellers: this.searchHotelsForm.get('travellers')?.value,
      checkIn: this.searchHotelsForm.get('checkIn')?.value,
      checkOut: this.searchHotelsForm.get('checkOut')?.value,
    }

    // storing the temporary array of data in the temporary storage service
    this.temporaryDetailStoreObj.storeHotelDetails(storeHotel);
    console.log(storeHotel);

    // routing the page to the hotel result page with hotelId for filtering and displaying hotel by id
    this.route.navigate(['/hotel-results'],
      { queryParams: { hotelId } }
    )
  }




}
