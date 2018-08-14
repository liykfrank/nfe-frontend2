
export class InputAmountServer {

  commission: number;
  fare: number;
  spam: number;
  tax: number; // TFC
  taxOnCommission: number; // TOCA

  constructor() {
    this.commission = 0;
    this.fare = 0;
    this.spam = 0;
    this.tax = 0;
    this.taxOnCommission = 0;
  }
}
