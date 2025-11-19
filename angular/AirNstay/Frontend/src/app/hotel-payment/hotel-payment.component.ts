import { Component, OnInit, Renderer2, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import HotelGuestDetails from '../HotelGuestDetails';
import { PaymentService } from '../payment-service.service';
import { TaxServiceService } from '../tax-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService } from '../hotel.service';
import HotelDetails from '../hotel-details';
import { TemopraryStoreService } from '../temoprary-store.service';
import { HttpResponse } from '@angular/common/http';
import { Location } from '@angular/common';
import { DOCUMENT } from '@angular/common';
import { ToastService } from '../toast.service';

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
    hotel_id: string;
  };
  theme: {
    color: string;
  };
}


@Component({
  selector: 'app-hotel-payment',
  standalone: false,
  templateUrl: './hotel-payment.component.html',
  styleUrl: './hotel-payment.component.css'
})
export class HotelPaymentComponent implements OnInit {

  // Razorpay Configuration (TEST KEY)
  readonly razorpayKeyId: string = 'rzp_test_Ra3nmIP3wYElZa';
  registrationForm!: FormGroup;
  selectedCouponDiscount: number = 0;
  selectedCouponCode: string = '';
  taxes!: number;
  totalAmount: number = 0;
  count: number = 0;
  adultFare!: number;
  selectedCountryCode: string = 'IN';
  selectedPaymentMethod: string = '';
  HotelGuestLst: HotelGuestDetails[] = [];
  hotelData: any = [];
  hotelLst: HotelDetails[] = [];
  pricePerNight!: number;
  bookingForm: FormGroup;
  termsForm: FormGroup;
  manualCouponCode: string = '';
  selectedCurrency: string = 'INR';
  hotelSearchDetails!: {
    checkIn: string;
    checkOut: string;
    travellers: number;
  };
  showHotelDetails: boolean = true;
  isEditMode: boolean = false;



  constructor(
    private fb: FormBuilder,
    private paymentHotelService: PaymentService,
    private taxService: TaxServiceService,
    private route: ActivatedRoute,
    private router: Router,
    private hotelServiceObj: HotelService,
    private temporaryDetailStoreObj: TemopraryStoreService,
    private location: Location,
    private renderer2: Renderer2,
    private toast : ToastService,
    @Inject(DOCUMENT) private document: Document
  ) {

    this.registrationForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s\-'.]+$/), Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s\-'.]+$/), Validators.minLength(2)]],
      age: ['', [Validators.required, Validators.min(13), Validators.max(120)]],
      gender: ['', Validators.required],
      id: ['']
    });

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
    let hotelId: string | null;

    this.route.queryParamMap.subscribe(params => {
      hotelId = params.get('hotelId');

      this.hotelServiceObj.getHotelById(hotelId).subscribe({
        next: (response: HttpResponse<any>) => {
          if (response.status == 200) {
            this.hotelLst = Array.isArray(response.body) ? response.body : [response.body];
            this.hotelData = this.hotelLst[0]; 
            this.adultFare = this.hotelData?.pricePerNight || 0;
            this.calculateTotal();
          }
        },
        error: (err) => alert(JSON.stringify(err)),
        complete: () => console.log("Hotel Fetched Succesfully"),
      })

      const storedDetails = this.temporaryDetailStoreObj.getStoredHotelDetails();
      if (storedDetails) {
        this.hotelSearchDetails = storedDetails;
        console.log("Hotel search details:", this.hotelSearchDetails);
      } else {
        console.warn("No hotel search details found");
      }
    });

    this.bookingForm.patchValue({
      email : localStorage.getItem('email'),
      phone : localStorage.getItem('contact')
      
    })

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

  public async submitHotelBooking(razorpayResponse?: any) {
    try {
      const userId = localStorage.getItem('userID');

      if (!userId) {
        console.error('User ID missing during booking submission. Cannot confirm booking.');
        return;
      }

      const bookingId = 'BKGHT' + Math.floor(Math.random() * 1000000);
      const paymentId = razorpayResponse?.razorpay_payment_id;
      const hotelDetails = this.hotelLst[0];
      const hotelId = hotelDetails.hotelId;
      const mappedGuests = this.HotelGuestLst.map(guest => ({
        bookingId: bookingId,
        firstName: guest.firstName,
        lastName: guest.lastName,
        gender: guest.gender,
        age: guest.age,
      }));

      const bookingData = {
        bookingId: bookingId,
        userId: userId,
        status: 'confirmed',
        type: 'hotel',
        bookingDate: new Date().toISOString(),
        hotelId: hotelDetails.hotelId,
        name: hotelDetails.name,
        checkInDate: this.hotelSearchDetails?.checkIn,
        checkOutDate: this.hotelSearchDetails?.checkOut,
        numberOfGuests: this.HotelGuestLst.length,
        address: this.hotelData.address,
        city: this.hotelData.city,
        travellers: mappedGuests,
        contactInfo: {
          bookingId: bookingId,
          country: this.bookingForm.get('country')?.value,
          email: this.bookingForm.get('email')?.value,
          phone: this.bookingForm.get('phone')?.value,
        },
        payment: {
          bookingId: bookingId,
          paymentId: paymentId,
          userId: userId,
          amount: this.totalAmount,
          method: 'Razorpay',
          status: 'confirmed',
          timestamp: new Date().toISOString(),
          razorpayDetails: razorpayResponse,
        },
      };

      this.router.navigate(['payment-success'],
        {
          queryParams: { hotelId, bookingId: bookingId, paymentId: paymentId }
        });

      console.log("Data to be send is :" + JSON.stringify(bookingData));

      // Submit booking data in the background
      this.paymentHotelService.submitHotelBooking(bookingData).subscribe({
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


  public async makePayment() {
    if (this.isDisabled) {
      console.error('Cannot proceed to payment: form invalid or no guests.');
      return;
    }

    const userId = localStorage.getItem('userID');

    if (!userId) {
      console.error('User authentication data is not ready. Razorpay blocked.');
      alert('Please log in or ensure your user session is active.');
      return;
    }

    // 1. Load the Razorpay SDK
    await this.loadScript('https://checkout.razorpay.com/v1/checkout.js');

    if (typeof Razorpay === 'undefined') {
      console.error('Razorpay SDK failed to load.');
      return;
    }

    const hotelDetails = this.hotelLst[0];
    const contactEmail = this.bookingForm.get('email')?.value;
    const contactPhone = this.bookingForm.get('phone')?.value;
    const contactName = this.HotelGuestLst.length > 0 ?
      `${this.HotelGuestLst[0].firstName} ${this.HotelGuestLst[0].lastName}` :
      'Guest User';

    // 2. Prepare Razorpay Options
    // Note: Assuming a similar INR conversion utility in temporaryDetailStoreObj if selectedCurrency is not INR
    const paymentAmountInINR = this.temporaryDetailStoreObj.getInrEquivalent ?
      this.temporaryDetailStoreObj.getInrEquivalent(this.totalAmount, this.selectedCurrency) : this.totalAmount;
    const amountInPaise = Math.round(paymentAmountInINR * 100);

    const options: RazorpayOptions = {
      key: this.razorpayKeyId,
      amount: amountInPaise.toString(),
      currency: 'INR',
      name: 'AirNStay Hotel Booking',
      description: `Hotel stay at ${hotelDetails.name}`,
      image: 'https://placehold.co/60x60/007bff/ffffff?text=AS',
      // This handler is called on a successful payment
      handler: (response: any) => {
        this.submitHotelBooking(response);
      },
      prefill: {
        name: contactName,
        email: contactEmail,
        contact: contactPhone,
      },
      notes: {
        address: this.bookingForm.get('country')?.value,
        hotel_id: hotelDetails.hotelId,
      },
      theme: {
        color: '#007bff',
      },
    };

    // 3. Open the Razorpay modal
    const rzp = new Razorpay(options);
    rzp.on('payment.failed', (response: any) => {
      console.error('Razorpay Failed:', response);
      alert('Payment failed. Please try again.');
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
    return this.HotelGuestLst.length;
  }

  get totalAdultFare(): number {
    const guests = this.HotelGuestLst.length;
    const roomsRequired = Math.max(1, Math.ceil(guests / 2));;
    return roomsRequired * this.adultFare;
  }

  get roomsRequired(): number {
    return Math.max(1, Math.ceil(this.HotelGuestLst.length / 2));
  }


  calculateTotal() {
    const guests = this.HotelGuestLst.length;
    const roomsRequired = Math.max(1, Math.ceil(guests / 2));
    const baseFare = roomsRequired * this.adultFare;

    this.taxes = this.taxService.getTaxAmount(baseFare, this.selectedCountryCode);
    this.totalAmount = baseFare + this.taxes - this.selectedCouponDiscount;
    this.temporaryDetailStoreObj.setHotelTotalAmount(this.totalAmount);
  }

  removeCoupon(): void {
    this.selectedCouponCode = '';
    this.selectedCouponDiscount = 0;
    this.calculateTotal();
  }
  setSelectedPaymentMethod(id: string): void {
    this.selectedPaymentMethod = id;
  }

  applyManualCoupon() {
    if (this.totalAmount === 0) {
      return;
    }

    const couponMap: any = {
      AVAILUPI: 300,
      FLAT350: 350,
      HDFC: 1000,
      NEW500: 500,
    };

    const discount = couponMap[this.manualCouponCode.toUpperCase()] || 0;
    this.applyCoupon(this.manualCouponCode, discount);
  }
  get isDisabled(): boolean {
    return this.HotelGuestLst.length === 0 ||
      !this.bookingForm.valid ||
      !this.termsForm.valid;
  }

  saveGuest() {
    
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }
    if (this.HotelGuestLst.length <= this.hotelSearchDetails.travellers) {

      const firstName = this.registrationForm.get('firstName')?.value;
      const lastName = this.registrationForm.get('lastName')?.value;
      const age = this.registrationForm.get('age')?.value;
      const gender = this.registrationForm.get('gender')?.value;
      const id = this.registrationForm.get('id')?.value;

      const guest = new HotelGuestDetails(firstName, lastName, gender, age, id);

      if (this.isEditMode) {
        const idx = this.HotelGuestLst.findIndex(g => g.id === id);
        if (idx !== -1) this.HotelGuestLst[idx] = guest;
        this.isEditMode = false;
      } else {
        if (!this.HotelGuestLst.some(g => g.id === id)) {
          this.HotelGuestLst.push(guest);
        }
      }

      this.registrationForm.reset();
      this.calculateTotal();
      this.count++;
    }
    if (this.count > this.hotelSearchDetails.travellers) {
      this.toast.showError("Number of guest can't be more than : " + this.hotelSearchDetails.travellers)
      return;
    }
  }

  editRecord(guest: HotelGuestDetails) {
    this.registrationForm.patchValue({
      firstName: guest.firstName,
      lastName: guest.lastName,
      gender: guest.gender,
      age: guest.age,
      id: guest.id
    });
    this.isEditMode = true;
  }
  deleteRecord(id: string) {
    this.HotelGuestLst = this.HotelGuestLst.filter(g => g.id !== id);
    this.calculateTotal();
  }


  onCurrencyChange(event: any) {
    this.selectedCurrency = event.target.value;
  }

  onbackbutton() {
    this.location.back();
  }
}