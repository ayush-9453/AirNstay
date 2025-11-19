import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'currencyConverter',
  standalone: false
})
export class CurrencyConverterPipe implements PipeTransform {
private exchangeRates: any = {
    INR: 1,
    USD: 0.012,
    EUR: 0.011,
    GBP: 0.0095
  };

  transform(value: number, currency: string): string {
    const rate = this.exchangeRates[currency] || 1;
    const converted = value * rate;
    const symbols: any = {
      INR: '₹',
      USD: '$',
      EUR: '€',
      GBP: '£'
    };

    return `${symbols[currency] || ''}${converted.toFixed(2)}`;
  }

}
