import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import hotelBookingDetails from './hotelBooking';
import { Observable } from 'rxjs';
import PackageBookingDetails from './packagebookingDetails';

@Injectable({
  providedIn: 'root'
})
export class TemopraryStoreService {


  
constructor() {
    // Load data from sessionStorage on service initialization
    const hotelDetails = sessionStorage.getItem('hotelSearchDetails');
    this.hotelSearchDetails = hotelDetails ? JSON.parse(hotelDetails) : null;

    const flightDetails = sessionStorage.getItem('flightSearchDetails');
    this.flightSearchDetails = flightDetails ? JSON.parse(flightDetails) : null;

    const hotelAmount = sessionStorage.getItem('hotelTotalAmount');
    this.hotelTotalAmount = hotelAmount ? +hotelAmount : 0;

    const flightAmount = sessionStorage.getItem('flightTotalAmount');
    this.flightTotalAmount = flightAmount ? +flightAmount : 0;

    const packageDetails = sessionStorage.getItem('selectedPackageDetails');
    this.selectedPackageDetails = packageDetails ? JSON.parse(packageDetails) : null;

    const packageAmount = sessionStorage.getItem('packageTotalAmount');
    this.packageTotalAmount = packageAmount ? +packageAmount : 0;
  }

  hotelSearchDetails: { checkIn: string; checkOut: string; travellers: number } | null = null;
  flightSearchDetails!: any;
  private hotelTotalAmount!: number;
  private flightTotalAmount!: number;
  private selectedPackageDetails!: PackageBookingDetails;
  private packageTotalAmount!: number;

  // Hotel Details
  storeHotelDetails(hotelDetails: { checkIn: string; checkOut: string; travellers: number }) {
    this.hotelSearchDetails = hotelDetails;
    sessionStorage.setItem('hotelSearchDetails', JSON.stringify(hotelDetails));
    console.log("Hotel details stored: ", this.hotelSearchDetails);
  }

  getStoredHotelDetails() {
    return this.hotelSearchDetails;
  }

  clearHotelDetails() {
    this.hotelSearchDetails = null;
    sessionStorage.removeItem('hotelSearchDetails');
  }

  // Hotel Amount
  setHotelTotalAmount(amount: number): void {
    this.hotelTotalAmount = amount;
    sessionStorage.setItem('hotelTotalAmount', amount.toString());
  }

  getHotelTotalAmount(): number {
    return this.hotelTotalAmount;
  }

  // Flight Amount
  setFlightTotalAmount(amount: number): void {
    this.flightTotalAmount = amount;
    sessionStorage.setItem('flightTotalAmount', amount.toString());
  }

  getFlightTotalAmount(): number {
    return this.flightTotalAmount;
  }

  // Package Amount
  setPackageTotalAmount(amount: number): void {
    this.packageTotalAmount = amount;
    sessionStorage.setItem('packageTotalAmount', amount.toString());
  }

  getPackageTotalAmount(): number {
    return this.packageTotalAmount;
  }

  // Package Details
  setSelectedPackageDetails(pkg: PackageBookingDetails): void {
    this.selectedPackageDetails = pkg;
    sessionStorage.setItem('selectedPackageDetails', JSON.stringify(pkg));
  }

  getSelectedPackageDetails(): PackageBookingDetails {
    return this.selectedPackageDetails;
  }

  /**
   * Converts an amount from a selected currency to its equivalent in INR (Indian Rupees).
   * This is necessary as Razorpay mandates INR for transactions.
   * NOTE: This uses hardcoded, mock exchange rates. In a real application, this should
   * call an external currency exchange API for live, accurate rates.
   * @param amount The total amount in the selected currency.
   * @param currency The currency code (e.g., 'USD', 'EUR').
   * @returns The equivalent amount in INR.
   */
  getInrEquivalent(amount: number, currency: string): number {
    const exchangeRates: { [key: string]: number } = {
        'INR': 1.0,
        'USD': 88.61, // Mock conversion: 1 USD ≈ 83.50 INR
        'EUR': 102.00, // Mock conversion: 1 EUR ≈ 91.00 INR
        'GBP': 115.00 // Mock conversion: 1 GBP ≈ 105.00 INR
    };

    const rate = exchangeRates[currency.toUpperCase()];
    
    if (rate) {
        // Convert the foreign currency amount to INR (Amount * Rate)
        return amount * rate;
    } else {
        console.warn(`Unknown currency: ${currency}. Assuming amount is already in INR.`);
        return amount;
    }
  }

  // ----------------------------------------------------------------

  // this is to store temporary hotel search details
  storeFlightSearchDetails(flightDetails: any) {
    this.flightSearchDetails = flightDetails;
    sessionStorage.setItem('flightSearchDetails', JSON.stringify(flightDetails));
    console.log("data is stored : ", this.flightSearchDetails);
  }

  getFlightSearchDetails() {
    return this.flightSearchDetails;
  }

  clearFlightSearchDetails() {
    this.flightSearchDetails = null;
    sessionStorage.removeItem('flightSearchDetails');
    console.log("Data is cleared : ", this.flightSearchDetails);
  }
}
