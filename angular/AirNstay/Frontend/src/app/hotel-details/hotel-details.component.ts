import { Component, ElementRef, ViewChild } from '@angular/core';
import { HotelService } from '../hotel.service';
import HotelDetails from '../hotel-details';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TemopraryStoreService } from '../temoprary-store.service';
import AllReviews from '../AllReviews';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RaiseTicketDataService } from '../raise-ticket-data.service';
import { storeUserId } from '../Auth-Store/auth.selector';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ToastService } from '../toast.service';


@Component({
  selector: 'app-hotel-details',
  standalone: false,
  templateUrl: './hotel-details.component.html',
  styleUrl: './hotel-details.component.css'
})
export class HotelDetailsComponent {
  stars = [1,2,3,4,5];
  ratings = 0;
  addReview! : FormGroup;
  currentPage = 1;
  itemsPerPage = 5;
  ReviewList! : AllReviews[];
  speficHotelReviews! : any[];
  isNavDisabled : boolean =true;

  
  hotelDetails!: HotelDetails[];


  // Rohit's review code
  filteredReviewList!: AllReviews[];
  userId$!: Observable<string | null>;



  constructor(private hotelServiceObj: HotelService, private route: ActivatedRoute,private toast : ToastService,
    private router: Router, private store: Store , private hotelDetailStoreObj: TemopraryStoreService, private fb : FormBuilder) {
      this.addReview = fb.group({
      rating : [null,Validators.required],
      name : ['',Validators.required],
      detailedReview : ['',Validators.required]
    })
  }



  ngOnInit() {
    let hotelId: string | null;

    this.route.queryParamMap.subscribe(params => {
      hotelId = params.get('hotelId');

      this.hotelServiceObj.getHotelById(hotelId).subscribe({
      next: (response : HttpResponse<any>) => {
        console.log("Response Obtained :"  +JSON.stringify(response));
        if(response.status == 200)
          this.hotelDetails = response.body;
       },
      error: (err) => console.log("Error Occurred :" + JSON.stringify(err)),
      complete: () => console.log("Hotel Fetched Succesfully"),
    })

    this.hotelServiceObj.getReviewByHotelID(hotelId).subscribe({
      next: (response : HttpResponse<any>) => {
        console.log("Response Obtained :"  +JSON.stringify(response));
        if(response.status == 200)
          this.ReviewList = response.body;
       },
      error: (err) => console.log("Error Occurred :" + JSON.stringify(err)),
      complete: () => console.log("Get Data Operation is Successfull...")

    })
    });

  }


  setRating(star: number): void {
  this.ratings = Math.max(star, 1);
  const validRating = star > 0 ? star : null;
  this.addReview.get('rating')?.setValue(validRating);
  this.addReview.get('rating')?.markAllAsTouched();
  }

  backToSearchHotel() {
    //to clear the hotel details in service
    this.hotelDetailStoreObj.clearHotelDetails();

    // to route back to hotel search page
    this.router.navigate(['/hotel']);
  }
   goToPayment(hotelId: string) {
    this.router.navigate(['/hotel-Payment'],
      { queryParams: { hotelId } });
  }

  
  // Reviews compontent ts file data 



    hotelId : string | null = '';
    

    getPaginatedReviews()
    {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      if(this.ReviewList?.length != 0)
      {
        this.isNavDisabled=false;
      }
      return this.ReviewList.slice(start, start+this.itemsPerPage)
    }
    get totalPages()
    {
      return Math.ceil(this.ReviewList.length / this.itemsPerPage);
    }
    changePage(page: number)
    {
      this.currentPage = page;
    }


    flooredValue() : number
    {
      return Math.floor(this.calculateAverageRating());
    }
  
    calculateAverageRating() : number
    {
        let sum = 0;
        for(let i=0;i<this.ReviewList.length;i++)
        {
          sum+=Number(this.ReviewList[i].rating);
        }
        if(sum == 0 || this.ReviewList.length == 0)
        {
          return 0;
        }
        return sum/this.ReviewList.length;
    }
    totalRating() : Number
    {
      let sum = 0;
        for(let i=0;i<this.ReviewList.length;i++)
        {
          sum+=Number(this.ReviewList[i].rating);
        }
        if(sum == 0)
        {
          return 0;
        }
        return sum;
    }
    totalReviews() : Number{
      return this.ReviewList.length;
    }
    countStarRating(star : number) : number
    {
      let cnt=0;
      for(let i=0;i<this.ReviewList.length;i++)
      {
        if(star === Number(this.ReviewList[i].rating))
        {
          cnt++;
        }
      }
      return cnt;
    }
    calculatePercentageofStar(star :number) : number
    {
        if(this.countStarRating(star)== 0)
        {
          return 0;
        }
       return (this.countStarRating(star)/this.ReviewList.length) * 100;
    }



  AddReview(){
    let reviewId = "R" + Math.floor(Math.random() * 1000);
    let rating = this.ratings.toFixed(1);
    let name = this.addReview.get(['name'])?.value;
    let detailedReview = this.addReview.get(['detailedReview'])?.value;
    let timestamp = new Date().toDateString();
    let userId : string = '';
    let hotelId : string | null = '';

    this.route.queryParamMap.subscribe(params => {
      hotelId = params.get('hotelId');
    })
    this.store.select(storeUserId).subscribe(id => {
      userId = id ?? '';
    })
    let newReview = new AllReviews(reviewId,rating,name,detailedReview,timestamp,userId, hotelId);

    console.log("New Review to be added :" + JSON.stringify(newReview));


    this.hotelServiceObj.AddReviewByHotelId(newReview).subscribe({
      next : (response : HttpResponse<any>) => {
        console.log("Response Obtained :" , +JSON.stringify(response));
        this.calculateAverageRating();
        this.ngOnInit();},
      error : (err) => {
        if(err.status == 403){
          this.toast.showError('Login to add review');
          return;
        }

      console.log("Review Cannot be added: " , +JSON.stringify(err));
      },
      complete : () => console.log("Insert Operation Successfully....")
    })

    
    this.ratings=0;
    this.addReview.reset();
  }

  Closed()
  {
    this.ratings=0;
    this.addReview.reset();
  }

  popupReview() {
    // alert("Directing to the add review options.")
  }

 



  @ViewChild('review') review! : ElementRef;
   scrollToReview(): void {
    this.review.nativeElement.scrollIntoView({ behavior: 'smooth' });
  }

  get name()
  {
    return this.addReview.get(['name']);
  }
  get detailedReview()
  {
    return this.addReview.get(['detailedReview']);
  }
  get rating()
  {
    return this.addReview.get(['rating']);
  }
}
