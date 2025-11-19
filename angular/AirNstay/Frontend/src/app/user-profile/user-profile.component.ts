import { ChangeDetectorRef, Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { RaiseTicketDataService } from '../raise-ticket-data.service';
import { GetBookingService } from '../get-booking.service';
import flightBookingDetails from '../flightBooking';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import hotelBookingDetails from '../hotelBooking';
import User from '../User';
import { ActivatedRoute, Router } from '@angular/router';
import { getUserId } from '../Auth-Store/auth.action';
import { AuthenticationService } from '../authentication.service';
import { HttpResponse } from '@angular/common/http';
import SupportTicket from '../SupportTicket';
import { ToastService } from '../toast.service';
import PackageBookingDetails from '../packagebookingDetails';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent {
  MyProfile: boolean = true;
  support: boolean = false;
  bAllBooking: boolean = true;
  bFlight: boolean = false;
  bHotel: boolean = true;
  bPackage: boolean = false;
  isDisalbed: boolean = true;
  bMyBooking: boolean = false;
  ticketData: SupportTicket[] = [];
  UserProfileData!: User[];
  bookedFlights: flightBookingDetails[] = [];
  bookedHotels: hotelBookingDetails[] = [];
  bookedPackages: PackageBookingDetails[] = [];
  UserProfile!: FormGroup;
  raiseTicket!: FormGroup;
  userId!: string | null;
  bTravellers: boolean = false;
  showModal = false;
  selectedBooking: any = null;
  Id!: string;

  constructor(
    private fb: FormBuilder,
    private ticketService: RaiseTicketDataService,
    private route: ActivatedRoute,
    private store: Store,
    private router: Router,
    private auth: AuthenticationService,
    private toastService: ToastService,
    private bookingObj: GetBookingService
  ) {
    this.UserProfile = this.fb.group({
      uname: ['', Validators.required],
      email: [{value : '' , disabled : true}, Validators.required],
      contactNumber: [null, Validators.required],
      gender: ['', Validators.required],
    });

    this.raiseTicket = this.fb.group({
      customerId: [''],
      name: ['', Validators.required],
      detailedIssue: ['', Validators.required],
    });
  }

  get name() {
    return this.UserProfile.get(['uname']);
  }
  get email() {
    return this.UserProfile.get(['email']);
  }
  get contactNumber() {
    return this.UserProfile.get(['contactNumber']);
  }
  get gender() {
    return this.UserProfile.get(['gender']);
  }
  get detailedIssue() {
    return this.raiseTicket.get(['detailedIssue']);
  }
  

  ngOnInit() {
    this.userId = localStorage.getItem('userID');
    this.getUserProfileData(this.userId)

    this.bookingObj.getBookedFlights(this.userId).subscribe({
      next: (response : HttpResponse<any>) => {
        if(response.status == 200)
          this.bookedFlights = response.body;
        console.log("Data obtained is :" +JSON.stringify(this.bookedFlights));
      },
      error: (err) => console.log(JSON.stringify(err)),
      complete: () => console.log("Data displayed successfully")
    })
    

    this.bookingObj.getBookedHotels(this.userId).subscribe({
      next: (response : HttpResponse<any>) => {
       if(response.status == 200)
          this.bookedHotels = response.body;
        console.log("Data obtained is :" +JSON.stringify(this.bookedHotels));
      },
      error: (err) => console.log(JSON.stringify(err)),
      complete: () => console.log("Data displayed of hotel successfully")
    })

    this.bookingObj.getBookedPackage(this.userId).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status == 200) this.bookedPackages = response.body;
        // console.log("Data obtained is :" +JSON.stringify(this.bookedPackages));
      },
      error: (err) =>  console.log(err.error),
      complete: () => console.log('Data displayed of Package successfully'),
    });


    
    this.route.queryParams.subscribe(params => {
      const section = params['section'];
      if (section === 'booking') {
        this.goToMyBookings();
      }
    })
    this.fetchTickets(this.userId);
  }

  
  goToMyBookings() {
    this.bMyBooking = true;
    this.MyProfile = false;
    this.support = false;
    this.ngOnInit();
  }
  // navigation methods 
  closeModal() {
    this.showModal = false;
  }



  cancelFlightBooking(bookingId: string) {
    this.bookingObj.cancelFlightBooking(bookingId).subscribe({
      next: (response : HttpResponse<any>) => {
        console.log("Flight booking cancelled successfully." + response.body);
      },
      error: (err) => console.log(JSON.stringify(err)),
      complete: () =>{ console.log("Flight Booking Cancel Operation Successfull..........");
        this.ngOnInit();
      }
    })
  }

  cancelHotelBooking(bookingId: string) {
    this.bookingObj.cancelHotelBooking(bookingId).subscribe({
      next: (response : HttpResponse<any>) => {
        console.log("Hotel booking cancelled successfully." +response.body);

      },
      error: (err) => console.log(JSON.stringify(err)),
      complete: () =>{ console.log("Hotel Booking Cancel Operation Successfull..........");
       this.ngOnInit();
      }
    })
  }

  cancelPackageBooking(bookingId : string){
    this.bookingObj.cancelPackageBooking(bookingId).subscribe({
      next: (response: HttpResponse<any>) => {
        console.log("Hotel booking cancelled successfully." +response.body);
    },
    error: (err) => console.log(JSON.stringify(err)),
    complete: () =>{ console.log('Package Booking Cancel Operation Successfull..........');
      this.ngOnInit();
    }
  });
  }

  GotoSupport() {
    this.MyProfile = false;
    this.bMyBooking = false;
    this.support = true;
    this.fetchTickets(this.userId);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  }
  formatTime(dateString: string): string {
    return new Date(dateString).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
    });
  }
  openBooking(booking: any) {
    this.selectedBooking = booking;
  }
  closeDialog() {
    this.selectedBooking = null;
  }

  RaiseTicket() {
    let customerId = this.userId;

    let name = this.raiseTicket.get(['name'])?.value;
    let detailedIssue = this.raiseTicket.get(['detailedIssue'])?.value;
    const ticketId = 'T' + Math.floor(1000 + Math.random() * 9000);
    this.Id = ticketId;
    let date = new Date();

    let newSupportTicket = new SupportTicket(
      ticketId,
      customerId,
      name,
      detailedIssue,
      date
    );
    console.log(newSupportTicket);
    this.ticketService.AddTickettoDB(newSupportTicket).subscribe({
      next: (response: HttpResponse<any>) => {
        if (response.status === 201) {
          // console.log('Ticket raised successfully:', response.body);
        }
      },
      error: (err) => {
        this.toastService.showError(err.error);
      },
      complete: () => {
        this.raiseTicket.reset({
          customerId: '',
          name: '',
          detailedIssue: '',
        });
        this.fetchTickets(this.userId);
        this.toastService.showSuccess('Ticket Created');
      },
    });
  }

  fetchTickets(userId: string | null) {
    // console.log('method getting call');

    if (!userId) {
      this.ticketData = []; 
      return;
    }
    this.ticketService.getTicketById(userId).subscribe({
      next: (data: SupportTicket[]) => {
        this.ticketData = data;
      },
      error: (err: any) => {
        console.error('Error fetching tickets:', err);
        this.ticketData = [];
      },
      complete: () => {},
    });
  }

    GotMyProfile() {
    this.MyProfile = true;
    this.support = false;
    this.bMyBooking = false;

    let userid: string | null = localStorage.getItem('userID');
    this.getUserProfileData(userid);
  }

  UserLogout() {
    this.MyProfile = false;
    this.support = false;
    localStorage.removeItem('userId');
    this.store.dispatch(getUserId({ userId: null }));
    this.auth.logout();
    if (this.userId == null) {
      this.router.navigate(['/LoginRegister']);
    }
  }

  getUserProfileData(userid: string | null) {
    this.ticketService.getUserProfileDataFromDB(userid).subscribe({
      next: (response: HttpResponse<any>) => {
        const obj = response.body;
        if (response.status == 200) {
          this.UserProfile.patchValue({
            uname: obj?.uname,
            email: obj?.email,
            contactNumber: obj?.contactNumber,
            gender: obj?.gender,
          });
        }
      },
      error: (err) => {
        if (err.status == 403){
           this.toastService.showError("Access denied");
           return;
        }
        this.toastService.showError(err.error);
      },
      complete: () =>{}
    });
  }

  MyAccountDetails() {
    const obj = this.UserProfileData.find(
      (user) => user.userID === this.userId
    );
    this.UserProfile.patchValue({
      uname: obj?.uname,
      email: obj?.email,
      contactNumber: obj?.contactNumber,
      gender: obj?.gender,
    });
  }

  
  UpdateUserData() {
    // const updatedData = this.UserProfile.value;
    localStorage.setItem('contact' , this.UserProfile.get('contactNumber')?.value);
    const updatedData = this.UserProfile.getRawValue();
    this.ticketService.updateUserData(this.userId, updatedData).subscribe({
      next: (data) => { 
        this.toastService.showSuccess('User Details Updated');
        console.log('Update operation successfull...');
      },
      error: (err) => {
        this.toastService.showError(err.error);
      },
      complete: () => {
        
       
      },
    });
    this.isDisalbed = true;
  }

  hotels() {
    this.bFlight = false;
    this.bHotel = true;
    this.bPackage = false;
    this.ngOnInit();
  }

  flights() {
    this.bFlight = true;
    this.bHotel = false;
    this.bPackage = false;
    this.ngOnInit();
  }
 
  packages() {
    this.bFlight = false;
    this.bHotel = false;
    this.bPackage = true;
    this.ngOnInit();
  }
  FormEnable() {
    this.isDisalbed = false;
  }
}
