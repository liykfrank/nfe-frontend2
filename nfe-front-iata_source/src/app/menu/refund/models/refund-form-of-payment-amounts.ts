export class RefundFormOfPaymentAmounts {
  amount: number;
  number: string;
  type: string;
  vendorCode: string;

  constructor() {
    this.amount = 0;
    this.number = '';
    this.type = '';
    this.vendorCode = '';
  }
}
