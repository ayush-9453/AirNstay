import { Component, OnInit, Renderer2, Inject } from '@angular/core';
import {FormBuilder,FormGroup,Validators} from '@angular/forms';
import { PaymentService } from '../payment-service.service';
import { TaxServiceService } from '../tax-service.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { PackageServiceService } from '../package-service.service';
import Packages from '../sub-package/Packages';
import { TemopraryStoreService } from '../temoprary-store.service';
import PackageGuestDetails from '../PackageGuestDetails';
import { Location, DOCUMENT } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
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
    package_id: string;
  };
  theme: {
    color: string;
  };
}

@Component({
  selector: 'app-package-payment',
  standalone: false,
  templateUrl: './package-payment.component.html',
  styleUrl: './package-payment.component.css',
})
export class PackagePaymentComponent implements OnInit {
  // Razorpay Configuration (TEST KEY)
  readonly razorpayKeyId: string = 'rzp_test_Ra3nmIP3wYElZa';
  registrationForm!: FormGroup;
  bookingForm: FormGroup;
  termsForm: FormGroup;
  PackageGuestLst: PackageGuestDetails[] = [];
  selectedCouponDiscount: number = 0;
  selectedCouponCode: string = '';
  taxes!: number;
  totalAmount: number = 0;
  baseFare: number = 0;
  totalBaseFare: number = 0;
  guestFareMultiplier: number = 1;
  selectedPaymentMethod: string = '';
  packageLst!: Packages[];
  selectedCountryCode: string = 'IN';
  filteredPackageById!: Packages[];
  selectedPackage!: Packages | undefined;
  manualCouponCode: string = '';
  selectedCurrency: string = 'INR';
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private paymentpackageservice: PaymentService,
    private taxService: TaxServiceService,
    private router: Router,
    private packageServiceObj: PackageServiceService,
    private route: ActivatedRoute,
    private location: Location,
    private temporaryDetailStoreObj: TemopraryStoreService,
    private renderer2: Renderer2,
    private toastService : ToastService,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.registrationForm = this.fb.group({
      firstName: [
        '', [
          Validators.required,
          Validators.pattern(/^[a-zA-Z\s\-'.]+$/),
          Validators.minLength(2),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[a-zA-Z\s\-'.]+$/),
          Validators.minLength(2),
        ],
      ],
      age: ['', [Validators.required, Validators.min(13), Validators.max(120)]],
      gender: ['', Validators.required],
      id: [''],
    });
    this.bookingForm = this.fb.group({
      country: ['India', [Validators.required]],
      phone: [
        '',
        [
          Validators.required,
          Validators.maxLength(10),
          Validators.minLength(10),
        ],
      ],
      email: ['', [Validators.required, Validators.email]],
    });

    this.termsForm = this.fb.group({
      termsAccepted: [false, Validators.requiredTrue],
    });
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

  ngOnInit(): void {
    // Read the package id from query params, validate and convert to number, then fetch package
    this.route.queryParamMap.subscribe((params) => {
      const idParam = params.get('id');
      const packageId = Number(idParam);
      if (Number.isNaN(packageId)) {
        console.error('Invalid package id in query params:', idParam);
        return;
      }
      this.packageServiceObj.getPackageById(packageId).subscribe({
        next: (response: HttpResponse<any>) => {
          if (response.status == 200) {
            this.selectedPackage = response.body;
            if (this.selectedPackage!) {
              this.baseFare = this.selectedPackage.price;
              this.calculateTotal();
              console.log('Selected Package:', this.selectedPackage);
            }
          }
        },
        error: (err) => this.toastService.showError(err.error),
        complete: () => console.log('Packages fetched successfully'),
      });
    });


    this.bookingForm.patchValue({
      email : localStorage.getItem('email'),
      phone : localStorage.getItem('contact'),
      
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

  public async submitPackageBooking(razorpayResponse?: any) {
    try {
      const userId = localStorage.getItem('userID');

      if (!userId) {
        console.error(
          'User ID missing during booking submission. Cannot confirm booking.'
        );
        return;
      }

      const bookingId = 'BKGPKG' + Math.floor(Math.random() * 1000000);
      const paymentId = razorpayResponse?.razorpay_payment_id;
      const pkg = this.selectedPackage;

      if (!pkg) {
        console.error('Selected package is undefined.');
        return;
      }

      const mappedGuests = this.PackageGuestLst.map((guest) => ({
        firstName: guest.firstName,
        lastName: guest.lastName,
        gender: guest.gender,
        age: guest.age,
      }));

      const bookingData = {
        bookingId: bookingId,
        userId: userId,
        status: 'confirmed',
        type: 'packages',
        bookingDate: new Date().toISOString(),

        packageTitle: pkg.title,
        duration: pkg.duration,
        location: pkg.location,
        agentId: pkg.AgentId,
        travellers: mappedGuests,
        contactInfo: {
          country: this.bookingForm.get('country')?.value,
          phone: this.bookingForm.get('phone')?.value,
          email: this.bookingForm.get('email')?.value,
        },
        payment: {
          paymentId: paymentId,
          amount: this.totalAmount,
          method: 'Razorpay',
          status: 'confirmed',
          timestamp: new Date().toISOString(),
          userId: userId,
          razorpayDetails: razorpayResponse,
        },
      };

      this.router.navigate(['payment-success'], {
        queryParams: {
          packageId: pkg.id,
          bookingId: bookingId,
          paymentId: paymentId,
        },
      });

      console.log('Data to be send is :' + JSON.stringify(bookingData));

      this.paymentpackageservice.submitPackageBooking(bookingData).subscribe({
        next: () => {
          console.log(
            'Booking confirmed and submitted successfully (Background Process).'
          );
        },
        error: (err) => {
          console.error(
            'Booking submission failed after payment (Background Process):',
            err
          );
        },
      });
    } catch (e) {
      console.error('Error during package booking submission process:', e);
    }
  }

  // Initiates the Razorpay payment process.

  async makePayment() {
    if (this.isDisabled || !this.selectedPackage) {
      console.error(
        'Cannot proceed to payment: form invalid, no guests, or no package selected.'
      );
      return;
    }

    // Get User ID from local storage
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

    const pkg = this.selectedPackage;
    const contactEmail = this.bookingForm.get('email')?.value;
    const contactPhone = this.bookingForm.get('phone')?.value;
    const contactName =
      this.PackageGuestLst.length > 0
        ? `${this.PackageGuestLst[0].firstName} ${this.PackageGuestLst[0].lastName}`
        : 'Guest User';

    const paymentAmountInINR = this.totalAmount;
    const amountInPaise = Math.round(paymentAmountInINR * 100);

    // 2. Prepare Razorpay Options
    const options: RazorpayOptions = {
      key: this.razorpayKeyId,
      amount: amountInPaise.toString(),
      currency: 'INR',
      name: 'AirNStay Package Booking',
      description: `Travel Package: ${pkg.title}`,
      image: 'https://placehold.co/60x60/007bff/ffffff?text=AS',
      // This handler is called on a successful payment
      handler: (response: any) => {
        this.submitPackageBooking(response);
      },
      prefill: {
        name: contactName,
        email: contactEmail,
        contact: contactPhone,
      },
      notes: {
        address: this.bookingForm.get('country')?.value,
        package_id: pkg.id?.toString() || 'N/A',
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

  applyCoupon(code: string, discount: number) {
    if (this.baseFare === 0) {
      return;
    }

    this.selectedCouponCode = code;
    this.selectedCouponDiscount = discount;
    this.calculateTotal();
  }

  calculateTotal() {
    const guestCount = this.PackageGuestLst.length || 1;
    this.totalBaseFare = this.baseFare * guestCount * this.guestFareMultiplier;

    this.taxes = this.taxService.getTaxAmount(
      this.totalBaseFare,
      this.selectedCountryCode
    );
    this.totalAmount = Math.max(
      this.totalBaseFare + this.taxes - this.selectedCouponDiscount,
      0
    );

    this.temporaryDetailStoreObj.setPackageTotalAmount(this.totalAmount);
  }

  removeCoupon(): void {
    this.selectedCouponCode = '';
    this.selectedCouponDiscount = 0;
    this.calculateTotal();
  }

  applyManualCoupon() {
    if (this.baseFare === 0) {
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
    return (
      this.PackageGuestLst.length === 0 ||
      !this.bookingForm.valid ||
      !this.termsForm.valid
    );
  }
  saveGuest() {
    if (this.registrationForm.invalid) {
      this.registrationForm.markAllAsTouched();
      return;
    }
    const firstName = this.registrationForm.get('firstName')?.value;
    const lastName = this.registrationForm.get('lastName')?.value;
    const age = this.registrationForm.get('age')?.value;
    const gender = this.registrationForm.get('gender')?.value;
    const id = this.registrationForm.get('id')?.value;

    const guest = new PackageGuestDetails(firstName, lastName, gender, age, id);

    if (this.isEditMode) {
      const idx = this.PackageGuestLst.findIndex((g) => g.id === id);
      if (idx !== -1) this.PackageGuestLst[idx] = guest;
      this.isEditMode = false;
    } else {
      if (!this.PackageGuestLst.some((g) => g.id === id)) {
        this.PackageGuestLst.push(guest);
      }
    }

    this.registrationForm.reset();
    this.calculateTotal();
  }

  editRecord(traveller: PackageGuestDetails) {
    this.registrationForm.patchValue({
      firstName: traveller.firstName,
      lastName: traveller.lastName,
      gender: traveller.gender,
      age: traveller.age,
      id: traveller.id,
    });
    this.isEditMode = true;
  }
  deleteRecord(id: string) {
    this.PackageGuestLst = this.PackageGuestLst.filter((g) => g.id !== id);
    this.calculateTotal();
  }

  onCurrencyChange(event: any) {
    this.selectedCurrency = event.target.value;
  }

  onbackbutton() {
    this.location.back();
  }
}
