import { Currency } from './currency.model';

export class CurrencyGet {
  isoc: string;
  currencies: Currency[] = [];

  constructor() {
    this.isoc = '';
    this.currencies = [];
  }
}
