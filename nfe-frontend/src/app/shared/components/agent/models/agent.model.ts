import { FormOfPayment } from './form-of-payment.model';

export class Agent {
  billingCity: string;
  billingCountry: string;
  billingPostalCode: string;
  billingStreet: string;
  defaultDate: string;
  iataCode: string;
  isoCountryCode: string;
  name: string;
  vatNumber: string;
  formOfPayment: FormOfPayment[];

  constructor() {
    this.billingCity = null;
    this.billingCountry = null;
    this.billingPostalCode = null;
    this.billingStreet = null;
    this.defaultDate = null;
    this.iataCode = null;
    this.isoCountryCode = null;
    this.name = null;
    this.vatNumber = null;
    this.formOfPayment = [];
  }
}
