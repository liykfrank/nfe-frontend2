import { FormArray, FormControl, Validators, FormGroup } from '@angular/forms';
import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

export class AmountRefundFormModel extends ReactiveFormHandlerModel {
  amountRefundGroup: FormGroup;
  amounts: FormGroup;

  taxMiscellaneousFees: FormArray;

  partialRefund: FormControl;
  netRemit: FormControl;
  grossFare: FormControl;
  lessGrossFareUsed: FormControl;
  // TODOTotal gros fare refunded?
  commissionAmount: FormControl;
  commissionRate: FormControl;
  dicountAmount: FormControl;
  cancellationPenalty: FormControl;
  taxOnCancellationPenalty: FormControl;
  miscellaneousFee: FormControl;
  taxOnMiscellaneousFee: FormControl;
  commissionOnCpAndMfAmount: FormControl;
  commissionOnCpAndMfRate: FormControl;
  tax: FormControl;
  refundToPassenger: FormControl;
  amountType: FormControl;
  amountTax: FormControl;

  constructor() {
    super();

    this.amountRefundGroup.get('partialRefund').valueChanges.subscribe(() => {
      console.log(this.amountRefundGroup.get('partialRefund').value);
      if (this.amountRefundGroup.get('partialRefund').value ) {
        this.amountRefundGroup.get('lessGrossFareUsed').enable();
      } else {
        this.amountRefundGroup.get('lessGrossFareUsed').enable();
      }
    });
  }

  createFormControls(): void {
    this.partialRefund = new FormControl('', [Validators.required]);
    this.netRemit = new FormControl('', [Validators.required]);
    this.grossFare = new FormControl('', []);
    this.lessGrossFareUsed = new FormControl({value: '' , disable: true}, []);
    // TODO Total gros fare refunded?
    this.commissionAmount = new FormControl('', []);
    this.commissionRate = new FormControl('', []);
    this.dicountAmount = new FormControl('', []);
    this.cancellationPenalty = new FormControl('', []);
    this.taxOnCancellationPenalty = new FormControl('', []);
    this.miscellaneousFee = new FormControl('', []);
    this.taxOnMiscellaneousFee = new FormControl('', []);
    this.commissionOnCpAndMfAmount = new FormControl('', []);
    this.commissionOnCpAndMfRate = new FormControl('', []);
    this.tax = new FormControl('', []);
    this.refundToPassenger = new FormControl('', []);
    this.amountType = new FormControl('', []);
    this.amountTax = new FormControl('', []);
  }

  createFormGroups(): void {
    this.taxMiscellaneousFees = new FormArray([
      // type: this.amountType,
      // amount: this.amountTax
    ]);

    this.amounts = new FormGroup({
      partialRefund: this.partialRefund,
      netRemit: this.netRemit,
      grossFare: this.grossFare,
      lessGrossFareUsed: this.lessGrossFareUsed,
      commissionAmount: this.commissionAmount,
      commissionRate: this.commissionRate,
      spam: this.dicountAmount,
      cancellationPenalty: this.cancellationPenalty,
      taxOnCancellationPenalty: this.taxOnCancellationPenalty,
      miscellaneousFee: this.miscellaneousFee,
      taxOnMiscellaneousFee: this.taxOnMiscellaneousFee,
      commissionOnCpAndMfAmount: this.commissionOnCpAndMfAmount,
      commissionOnCpAndMfRate: this.commissionOnCpAndMfAmount,
      tax: this.tax, //TODO : preguntar
      refundToPassenger: this.refundToPassenger,
      taxMiscellaneousFees: this.taxMiscellaneousFees,
    });
  }

  createForm(): void {
    this.amountRefundGroup = new FormGroup({
      partialRefund: this.partialRefund,
      amounts: this.amounts
    });
  }

}
