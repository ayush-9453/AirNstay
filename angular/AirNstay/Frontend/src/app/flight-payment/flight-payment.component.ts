import { Component, OnInit, Renderer2, Inject} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import TravellerDetails from '../travellerDetails';
import { PaymentService } from '../payment-service.service';
import { TaxServiceService } from '../tax-service.service';
import { FlightServiceService } from '../flight-service.service';
import FlightDetails from '../flight-details';
import { ActivatedRoute, Router } from '@angular/router';
import { TemopraryStoreService } from '../temoprary-store.service';
import { Location } from '@angular/common';
import { DOCUMENT } from '@angular/common';
import { HttpResponse } from '@angular/common/http';

declare var Razorpay: any;

interface RazorpayOptions {
 key: string;
 amount: string;
 currency: string;
 name: string;
 description: string;
 order_id?: string;
 image: string;
 handler: (response: any) => void;
 prefill: {
  name: string;
  email: string;
  contact: string;
 };
 notes: {
  address: string;
  flight_id: string;
 };
 theme: {
  color: string;
 };
}

@Component({
 selector: 'app-flight-payment',
 standalone: false,
 templateUrl: './flight-payment.component.html',
 styleUrl: './flight-payment.component.css'
})

export class FlightPaymentComponent implements OnInit {

 // Razorpay Configuration (TEST KEY)
 readonly razorpayKeyId: string = 'rzp_test_Ra3nmIP3wYElZa';
 showFormAdult: boolean = true;
 showFormChildren: boolean = false;
 registrationForm!: FormGroup;
 bookingForm: FormGroup;
 termsForm: FormGroup;
 TravellerLst: TravellerDetails[] = [];
 selectedCouponDiscount: number = 0;
 selectedCouponCode: string = '';
 taxes!: number;
 totalAmount: number = 0;
 adultFare!: number;
 userId! : string | null;
 childFare!: number;
 selectedCountryCode: string = 'IN';
 selectedPaymentMethod: string = '';
 flightLst: FlightDetails[] = [];
 flightSearchDetails!: any;
 isEditMode: boolean = false;
 manualCouponCode: string = '';
 selectedCurrency: string = 'INR';


 constructor(
  private fb: FormBuilder,
  private paymentflightservice: PaymentService,
  private taxService: TaxServiceService,
  private FlightServiceObj: FlightServiceService,
  private route: ActivatedRoute,
  private temporaryDetailStoreObj: TemopraryStoreService,
  private router: Router,
  private renderer2: Renderer2,
  private location: Location,
  @Inject(DOCUMENT) private document: Document
 ) {
 

  this.registrationForm = this.fb.group({
   firstName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s\-'.]+$/), Validators.minLength(2)]],
   lastName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s\-'.]+$/), Validators.minLength(2)]],
   age: ['', [Validators.required, Validators.min(13), Validators.max(120)]],
   gender: ['', Validators.required],
   id: ['']

  });

  // Explicitly set validators for Adult age range on initialization since showFormAdult is true by default
  this.registrationForm.get('age')?.setValidators([Validators.required, Validators.min(13), Validators.max(120)]);
  this.registrationForm.get('age')?.updateValueAndValidity();


  this.bookingForm = this.fb.group({
   country: ['India', [Validators.required]],
   phone: ['', [Validators.required, Validators.maxLength(10), Validators.minLength(10)]],
   email: ['', [Validators.required, Validators.email]],
  });

  this.termsForm = this.fb.group({
   termsAccepted: [false, Validators.requiredTrue],
  });
 }

 ngOnInit() {
  this.userId = localStorage.getItem('userID');

  let flightId: string | null;
  this.route.queryParamMap.subscribe(params => {
   flightId = params.get('flightId');

   if (flightId) {
    this.FlightServiceObj.getFlightById(flightId).subscribe({
     next: (response: HttpResponse<any>) => {
      if (response.status == 200) {
       this.flightLst = [response.body] as FlightDetails[];
       console.log("Response Obtained :" + JSON.stringify(this.flightLst));
       const selectedFlight = this.flightLst[0];
       this.adultFare = selectedFlight.price;
       this.childFare = selectedFlight.price * 0.5;
       this.calculateTotal();
      }

     },
     error: (err) => console.error("Flight Fetching Failed:", err),
     complete: () => console.log("Flight Fetched Succesfully"),
    })

    this.bookingForm.patchValue({
      email : localStorage.getItem('email'),
      phone : localStorage.getItem('contact')
      
    })

  }

  });
  this.flightSearchDetails = this.temporaryDetailStoreObj.getFlightSearchDetails();
 }

 public selectTravellerType(type: string) {
  const ageControl = this.registrationForm.get('age');
  if (type === 'Adult') {
   this.showFormAdult = true;
   this.showFormChildren = false;
   ageControl?.setValidators([Validators.required, Validators.min(13), Validators.max(120)]);
  } else if (type === 'Child') {
   this.showFormAdult = false;
   this.showFormChildren = true;
   ageControl?.setValidators([Validators.required, Validators.min(1), Validators.max(12)]);
  }
  ageControl?.updateValueAndValidity();

  this.registrationForm.reset();
  this.isEditMode = false;
  this.calculateTotal(); 
 }


 public loadScript(src: string): Promise<void> {
  return new Promise((resolve) => {
   // Check if script is already loaded
   if (this.document.getElementById('razorpay-checkout-js')) {
    return resolve();
   }

   const script = this.renderer2.createElement('script');
   script.id = 'razorpay-checkout-js';
   script.src = src;

   script.onload = () => resolve();
   script.onerror = () => {
    console.error('Failed to load Razorpay SDK');
    resolve();
   };

   this.renderer2.appendChild(this.document.body, script);
  });
 }


 public async submitFlightBooking(razorpayResponse?: any) {
  
  try { 

    if (!this.userId) {
      console.error('User ID missing during booking submission. Cannot confirm booking.');
      return; 
    }
    const bookingId = 'BKGFT' + Math.floor(Math.random() * 1000000);
    const paymentId = razorpayResponse?.razorpay_payment_id;
    const flightDetails = this.flightLst[0];
    const flightId = flightDetails.flightId;

   const bookingData= {
    bookingId: bookingId,
    status: 'confirmed',
    type: 'flight',
    userId: this.userId,
    bookingDate: new Date().toISOString(),
    flightId: flightDetails.flightId,
    airline: flightDetails.airline,
    departure: flightDetails.departure,
    arrival: flightDetails.arrival,
    departureDate: flightDetails.departureDate,
    arrivalDate: flightDetails.arrivalDate,
    duration: flightDetails.duration,
    classType: flightDetails.classType,
    travellers: this.TravellerLst,
    contactInfo: {
     bookingId: bookingId,
     country: this.bookingForm.get('country')?.value,
     email: this.bookingForm.get('email')?.value,
     phone: this.bookingForm.get('phone')?.value,
    },
    payment: {
     bookingId: bookingId,
     paymentId: paymentId,
     userId: this.userId, 
     amount: this.totalAmount,
     method: 'Razorpay',
     status: 'confirmed',
     timestamp: new Date().toISOString(),
     razorpayDetails: razorpayResponse,
    },
   };

   this.router.navigate(['payment-success'], { queryParams: { flightId, bookingId: bookingId, paymentId: paymentId} });
   console.log("payment data is :" +JSON.stringify(bookingData));

   this.paymentflightservice.submitFlightBooking(bookingData).subscribe({
    next: () => {
     console.log('Booking confirmed and submitted successfully (Background Process).');
    },
    error: (err) => {
     console.error('Booking submission failed after payment (Background Process):', err);
    }
   });
  } catch (e) {
   console.error("Error during booking submission process:", e);
  }
 }


 public async makePayment(flightId: string) {
  if (this.isDisabled) {
   console.error('Cannot proceed to payment: form invalid');
   return;
  }
    


  const userId = localStorage.getItem('userID'); 

  if (!userId) {
   console.error('User authentication data is not ready. Razorpay blocked.');
   return;
  }

  // 1. Load the Razorpay SDK
  await this.loadScript('https://checkout.razorpay.com/v1/checkout.js');

  if (typeof Razorpay === 'undefined') {
   console.error('Razorpay SDK failed to load.');
   return;
  }

  const flightDetails = this.flightLst[0];
  const contactEmail = this.bookingForm.get('email')?.value;
  const contactPhone = this.bookingForm.get('phone')?.value;
  const contactName = this.TravellerLst.length > 0 ?
   `${this.TravellerLst[0].firstName} ${this.TravellerLst[0].lastName}` :
   'Guest User';

  // 2. Prepare Razorpay Options
  const paymentAmountInINR = this.temporaryDetailStoreObj.getInrEquivalent ?
   this.temporaryDetailStoreObj.getInrEquivalent(this.totalAmount, this.selectedCurrency) : this.totalAmount;
  const amountInPaise = Math.round(paymentAmountInINR * 100);

  const options: RazorpayOptions = {
   key: this.razorpayKeyId,
   amount: amountInPaise.toString(),
   currency: 'INR',
   name: 'AirNStay Flight Booking',
   description: `Flight from ${flightDetails.departure} to ${flightDetails.arrival}`,
   image: 'https://placehold.co/60x60/007bff/ffffff?text=AS',
   // This handler is called on a successful payment
   handler: (response: any) => {
    this.submitFlightBooking(response);
   },
   prefill: {
    name: contactName,
    email: contactEmail,
    contact: contactPhone,
   },
   notes: {
    address: this.bookingForm.get('country')?.value,
    flight_id: flightDetails.flightId,
   },
   theme: {
    color: '#007bff',
   },
  };

  // 3. Open the Razorpay modal
  const rzp = new Razorpay(options);
  rzp.on('payment.failed', (response: any) => {
   console.error('Razorpay Failed:', response);
  });
  rzp.open();
 }

 get firstName() {
  return this.registrationForm.get('firstName');
 }
 get lastName() {
  return this.registrationForm.get('lastName');
 }
 get age() {
  return this.registrationForm.get('age');
 }


 applyCoupon(code: string, discount: number) {
  if (this.totalAmount === 0) {
   return;
  }

  this.selectedCouponCode = code;
  this.selectedCouponDiscount = discount;
  this.calculateTotal();
 }
 get adultCount(): number {
  return this.TravellerLst.filter(t => t.type === 'Adult').length;
 }

 get childCount(): number {
  return this.TravellerLst.filter(t => t.type === 'Child').length;
 }

 get totalAdultFare(): number {
  return this.adultCount * this.adultFare;
 }

 get totalChildFare(): number {
  return this.childCount * this.childFare;
 }

 calculateTotal() {
  const adultCount = this.adultCount || 1;
  const childCount = this.childCount;
  const baseFare = (adultCount * this.adultFare) + (childCount * this.childFare);
  this.taxes = this.taxService.getTaxAmount(baseFare, this.selectedCountryCode);
  this.totalAmount = Math.max(baseFare + this.taxes - this.selectedCouponDiscount, 0);
  this.temporaryDetailStoreObj.setFlightTotalAmount(this.totalAmount);
 }

 applyManualCoupon() {

  if (this.totalAmount === 0) {
   return;
  }

  const couponMap: any = {
   'AVAILUPI': 300,
   'FLAT350': 350,
   'HDFC': 1000,
   'NEW500': 500
  };

  const discount = couponMap[this.manualCouponCode.toUpperCase()] || 0;
  this.applyCoupon(this.manualCouponCode, discount);
 }
 removeCoupon(): void {
  this.selectedCouponCode = '';
  this.selectedCouponDiscount = 0;
  this.calculateTotal();
 }

 get isDisabled(): boolean {
  return this.TravellerLst.length === 0 ||
   this.bookingForm.invalid ||
   this.termsForm.invalid;
 }

 saveTraveller() {
  if (this.registrationForm.invalid) {
   this.registrationForm.markAllAsTouched();
   return;
  }
  const firstName = this.registrationForm.get('firstName')?.value;
  const lastName = this.registrationForm.get('lastName')?.value;
  const age = this.registrationForm.get('age')?.value;
  const gender = this.registrationForm.get('gender')?.value;
  const id = this.registrationForm.get('id')?.value;
  const type = age >= 13 ? 'Adult' : 'Child';

  const guest = new TravellerDetails(firstName, lastName, gender, age, id, type);

  if (this.isEditMode) {
   const idx = this.TravellerLst.findIndex(g => g.id === id);
   if (idx !== -1) this.TravellerLst[idx] = guest;
   this.isEditMode = false;
  } else {
   if (!this.TravellerLst.some(g => g.id === id)) {
    this.TravellerLst.push(guest);
   }
  }

  this.registrationForm.reset();
  this.calculateTotal();
 }

 editRecord(traveller: TravellerDetails) {
  this.registrationForm.patchValue({
   firstName: traveller.firstName,
   lastName: traveller.lastName,
   gender: traveller.gender,
   age: traveller.age,
   id: traveller.id
  });
  this.isEditMode = true;

  if (traveller.type === 'Adult') {
   this.showFormAdult = true;
   this.showFormChildren = false;
  } else if (traveller.type === 'Child') {
   this.showFormChildren = true;
   this.showFormAdult = false;
  }
 }

 deleteRecord(id: string) {
  this.TravellerLst = this.TravellerLst.filter(g => g.id !== id);
  this.calculateTotal();
 }

 onCurrencyChange(event: any) {
  this.selectedCurrency = event.target.value;
 }

 onbackbutton() {
  this.location.back();
 }

}