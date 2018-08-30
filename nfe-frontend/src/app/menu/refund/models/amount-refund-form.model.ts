import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { GLOBALS } from './../../../shared/constants/globals';

export class AmountRefundFormModel extends ReactiveFormHandlerModel {
  amountRefundGroup: FormGroup;
  amounts: FormGroup;

  taxMiscellaneousFees: FormArray;

  partialRefund: FormControl;
  netRemit: FormControl;
  grossFare: FormControl;
  lessGrossFareUsed: FormControl;
  totalGrossFareRefunded: FormControl;
  // TODOTotal gros fare refunded?
  commissionAmount: FormControl;
  commissionRate: FormControl;
  spam: FormControl;
  tax: FormControl;
  cancellationPenalty: FormControl;
  taxOnCancellationPenalty: FormControl;
  miscellaneousFee: FormControl;
  taxOnMiscellaneousFee: FormControl;
  commissionOnCpAndMfAmount: FormControl;
  commissionOnCpAndMfRate: FormControl;
  refundToPassenger: FormControl;
  amountType: FormControl;
  amountTax: FormControl;

  radioCommission: FormControl;
  radioCpAndMfCommission: FormControl;

  constructor() {
    super();
  }

  createFormControls(): void {
    this.partialRefund = new FormControl(false);
    this.netRemit = new FormControl(false);
    this.grossFare = new FormControl({
      value: '0',
      disabled: this.partialRefund.value
    });
    this.lessGrossFareUsed = new FormControl({
      value: '0',
      disabled: this.partialRefund.value
    });
    this.totalGrossFareRefunded = new FormControl({
      value: '0',
      disabled: true
    });
    this.tax = new FormControl(0);
    this.radioCommission = new FormControl('');
    this.radioCpAndMfCommission = new FormControl('');
    // TODO Total gros fare refunded?
    this.commissionAmount = new FormControl({
      value: '0',
      disabled: false
    }); /*?:99 | */
    this.commissionRate = new FormControl({ value: '', disabled: true }, [
      Validators.pattern(GLOBALS.PATTERNS.PERCENT_MAX_99_99)
    ]);

    this.spam = new FormControl({ value: '0', disabled: true });
    this.cancellationPenalty = new FormControl('0');
    this.taxOnCancellationPenalty = new FormControl('');
    this.miscellaneousFee = new FormControl('');
    this.taxOnMiscellaneousFee = new FormControl('');
    this.commissionOnCpAndMfAmount = new FormControl({
      value: '',
      disabled: false
    });
    this.commissionOnCpAndMfRate = new FormControl(
      { value: '', disabled: true },
      [Validators.pattern(GLOBALS.PATTERNS.PERCENT_MAX_99_99)]
    );
    this.refundToPassenger = new FormControl(0);
    this.amountType = new FormControl('', [
      Validators.pattern(GLOBALS.PATTERNS.TAX)
    ]);
    this.amountTax = new FormControl('0');
  }

  createFormGroups(): void {
    this.taxMiscellaneousFees = new FormArray(
      [new FormGroup({
        type: new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.TAX)]),
        amount: new FormControl(0)
      })]);

    this.amounts = new FormGroup({
      grossFare: this.grossFare,
      lessGrossFareUsed: this.lessGrossFareUsed,
      totalGrossFareRefunded: this.totalGrossFareRefunded,
      commissionAmount: this.commissionAmount,
      commissionRate: this.commissionRate,
      tax: this.tax,
      amountType: this.amountType,
      amountTax: this.amountTax,
      radioCommission: this.radioCommission,
      radioCpAndMfCommission: this.radioCpAndMfCommission,
      spam: this.spam,
      cancellationPenalty: this.cancellationPenalty,
      taxOnCancellationPenalty: this.taxOnCancellationPenalty,
      miscellaneousFee: this.miscellaneousFee,
      taxOnMiscellaneousFee: this.taxOnMiscellaneousFee,
      commissionOnCpAndMfAmount: this.commissionOnCpAndMfAmount,
      commissionOnCpAndMfRate: this.commissionOnCpAndMfRate,
      refundToPassenger: this.refundToPassenger,
      taxMiscellaneousFees: this.taxMiscellaneousFees
    });
  }

  createForm(): void {
    this.amountRefundGroup = new FormGroup({
      partialRefund: this.partialRefund,
      netRemit: this.netRemit,
      amounts: this.amounts
    });
  }
}
