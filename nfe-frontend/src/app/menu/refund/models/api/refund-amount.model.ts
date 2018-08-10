export class RefundAmount {
  cancellationPenalty: number;
  commissionAmount: number;
  commissionOnCpAndMfAmount: number;
  commissionOnCpAndMfRate: number;
  commissionRate: number;
  grossFare: number;
  lessGrossFareUsed: number;
  miscellaneousFee: number;
  refundToPassenger: number;
  spam: number;
  tax: number;
  taxOnCancellationPenalty: number;
  taxOnMiscellaneousFee: number;

  constructor() {
    this.cancellationPenalty = 0;
    this.commissionAmount = 0;
    this.commissionOnCpAndMfAmount = 0;
    this.commissionOnCpAndMfRate = 0;
    this.commissionRate = 0;
    this.grossFare = 0;
    this.lessGrossFareUsed = 0;
    this.miscellaneousFee = 0;
    this.refundToPassenger = 0;
    this.spam = 0;
    this.tax = 0;
    this.taxOnCancellationPenalty = 0;
    this.taxOnMiscellaneousFee = 0;
  }
}
