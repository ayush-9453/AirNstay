import { Component, OnInit } from '@angular/core';
import { TemopraryStoreService } from '../temoprary-store.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-payment-success',
  standalone: false,
  templateUrl: './payment-success.component.html',
  styleUrl: './payment-success.component.css'
})
export class PaymentSuccessComponent implements OnInit {
  userId: string = '';
  bookingType: string = '';
  paymentSummary: any | undefined;
  bookingId: string = '';
  paymentId: string = '';

  constructor(
    private temporaryStoreObj: TemopraryStoreService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit() {
    this.userId = localStorage.getItem('userID') || '';

    this.route.queryParamMap.subscribe(params => {

      this.bookingId = params.get('bookingId') || '';
      this.paymentId = params.get('paymentId') || '';
      const hotelId = params.get('hotelId');
      const flightId = params.get('flightId');
      const packageId = params.get('packageId');

      if (hotelId) {
       this.sendHotelPaymentData();
      }

      if (flightId) {
        this.sendFlightPaymentData();
      }

      if (packageId) {
        this.sendPackagePaymentData();
      }
    });
  }

  sendHotelPaymentData() {

    const totalAmount = (this.temporaryStoreObj as any).getHotelTotalAmount?.() ?? 0;

    const paymentData = {
      paymentId: this.paymentId,
      status: 'confirmed',
      bookingId: this.bookingId,
      userId: this.userId,
      price: totalAmount,
      type: 'hotel'
    };

    this.paymentSummary = paymentData;

  }

  sendFlightPaymentData() {

    const totalAmount = this.temporaryStoreObj.getFlightTotalAmount();

    const flightData= {
      bookingId: this.bookingId,
      paymentId: this.paymentId,
      price: totalAmount,
      status: 'confirmed',
      type: 'flight'
    };

    this.paymentSummary = flightData;

  }

  sendPackagePaymentData() {

    const totalAmount = this.temporaryStoreObj.getPackageTotalAmount();

    const packageData= {
      bookingId: this.bookingId,
      paymentId: this.paymentId,
      price: totalAmount,
      status: 'confirmed',
      type: 'package'
    };

    this.paymentSummary = packageData;

  }


goToHomepage(): void {
  const queryParams = this.route.snapshot.queryParamMap;
  const hotelId = queryParams.get('hotelId');
  const flightId = queryParams.get('flightId');
  const packageId = queryParams.get('packageId');

  if (flightId) {
    this.router.navigate(['/flights']);
  } else if (hotelId) {
    this.router.navigate(['/hotel']);
  } else if (packageId) {
    this.router.navigate(['/packages']);
  } else {
    this.router.navigate(['/']);
  }
}


  handleViewBooking(): void {
    this.router.navigate(['/userProfile']);
  }
}
