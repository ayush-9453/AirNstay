import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TaxServiceService {

  constructor() { }
  
private taxRates: { [countryCode: string]: number } = {
  'IN': 0.18,
  'US': 0.07,
  'UK': 0.20,
  'DE': 0.19
};



getTaxAmount(baseFare: number, countryCode: string): number {
  const rate = this.taxRates[countryCode.toUpperCase()] || 0;
  const tax = baseFare * rate;
  return parseFloat(tax.toFixed(2));
}


 getTaxRate(countryCode: string): number {
  const rate = this.taxRates[countryCode.toUpperCase()] || 0;
  return parseFloat(rate.toFixed(2));
}


}
