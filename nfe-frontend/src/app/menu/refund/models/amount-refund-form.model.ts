import { GLOBALS } from './../../../shared/constants/globals';
import { OnDestroy } from '@angular/core';
import { FormArray, FormControl, Validators, FormGroup } from '@angular/forms';
import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { CurrencyService } from '../../../shared/components/currency/services/currency.service';
import { Currency } from '../../../shared/components/currency/models/currency.model';

export class AmountRefundFormModel extends ReactiveFormHandlerModel implements OnDestroy {
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
  dicountAmount: FormControl;
  totalTaxes: FormControl;
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

  radioCommission: FormControl;
  radioCpAndMfCommission: FormControl;

  public decimals = 0;


  private suscrNetRemit;
  private suscriptionFormGeneral;
  private suscrRadio;
  private suscrRadioCpMp;
  private suscrPartialRefund;
  private suscrGrossFare;
  private suscrLessGrossFare;


  constructor() {
    super();
    this.setSubscriptions();

  }

  ngOnDestroy() {
    this.removeSubscriptions();
  }

  getValueForTotalGrossFareRefunded() {
    return (this.grossFare.value - this.lessGrossFareUsed.value).toFixed(this.decimals);
  }

  createFormControls(): void {
    this.partialRefund = new FormControl(false);
    this.netRemit = new FormControl(false);
    this.grossFare = new FormControl({ value: '0', disabled: this.partialRefund.value });
    this.lessGrossFareUsed = new FormControl({ value: '0', disabled: this.partialRefund.value });
    this.totalGrossFareRefunded = new FormControl({ value: '0', disabled: true });
    this.totalTaxes = new FormControl({ value: '0', disabled: true });
    this.radioCommission = new FormControl('');
    this.radioCpAndMfCommission = new FormControl('');
    // TODO Total gros fare refunded?
    this.commissionAmount = new FormControl({ value: '0', disabled: false }); /*?:99 | */
    this.commissionRate = new FormControl({ value: '', disabled: true }, [Validators.pattern(GLOBALS.PATTERNS.PERCENT_MAX_99_99)]);

    this.dicountAmount = new FormControl({ value: '0', disabled: true });
    this.cancellationPenalty = new FormControl('0');
    this.taxOnCancellationPenalty = new FormControl('');
    this.miscellaneousFee = new FormControl('');
    this.taxOnMiscellaneousFee = new FormControl('');
    this.commissionOnCpAndMfAmount = new FormControl({ value: '', disabled: false });
    this.commissionOnCpAndMfRate = new FormControl({ value: '', disabled: true }, [Validators.pattern(GLOBALS.PATTERNS.PERCENT_MAX_99_99)]);
    this.tax = new FormControl('');
    this.refundToPassenger = new FormControl(0);
    this.amountType = new FormControl('');
    this.amountTax = new FormControl('0');
  }

  createFormGroups(): void {
    this.taxMiscellaneousFees = new FormArray([]);

    this.amounts = new FormGroup({
      grossFare: this.grossFare,
      lessGrossFareUsed: this.lessGrossFareUsed,
      totalGrossFareRefunded: this.totalGrossFareRefunded,
      commissionAmount: this.commissionAmount,
      commissionRate: this.commissionRate,
      totalTaxes: this.totalTaxes,
      amountType: this.amountType,
      amountTax: this.amountTax,
      radioCommission: this.radioCommission,
      radioCpAndMfCommission: this.radioCpAndMfCommission,
      spam: this.dicountAmount,
      cancellationPenalty: this.cancellationPenalty,
      taxOnCancellationPenalty: this.taxOnCancellationPenalty,
      miscellaneousFee: this.miscellaneousFee,
      taxOnMiscellaneousFee: this.taxOnMiscellaneousFee,
      commissionOnCpAndMfAmount: this.commissionOnCpAndMfAmount,
      commissionOnCpAndMfRate: this.commissionOnCpAndMfRate,
      tax: this.tax,
      refundToPassenger: this.refundToPassenger,
      taxMiscellaneousFees: this.taxMiscellaneousFees,
    });
  }

  createForm(): void {
    this.amountRefundGroup = new FormGroup({
      partialRefund: this.partialRefund,
      netRemit: this.netRemit,
      amounts: this.amounts
    });
    this.amountType.setValue('', {emitEvent: true});
  }

  setValueAmounts() {
    return (0).toFixed(this.decimals);
  }

  addTaxes() {


    if (this.amountType.value != '') {

      this.taxMiscellaneousFees.push(this.newTaxes(this.amountType.value, this.amountTax.value));

      setTimeout(() => {

        this.amountType.setValue('');
        this.amountTax.setValue(this.setValueAmounts());
      }, 0);
      this.updateTotalTaxes();
    } else {
      this.amountType.setErrors({ 'required': true });
      this.amountType.markAsTouched();
    }
  }

  updateTotalTaxes() {
    let total = 0;
    for (const tax of this.taxMiscellaneousFees.controls) {
      total = total + Number(tax.value.amount);
    }
    this.totalTaxes.setValue(total.toFixed(this.decimals), { emitEvent: true });
  }

  updateRefundToPassenger() {
    let result = 0;

    if (this.partialRefund.value == 'true') {
      result = this.getNumber(this.grossFare.value)
        + this.getNumber(this.totalTaxes.value)
        - this.getNumber(this.cancellationPenalty.value)
        - this.getNumber(this.taxOnCancellationPenalty.value)
        - this.getNumber(this.miscellaneousFee.value)
        - this.getNumber(this.taxOnMiscellaneousFee.value);

    } else {

      result = this.getNumber(this.totalGrossFareRefunded.value)
        + this.getNumber(this.totalTaxes.value)
        - this.getNumber(this.cancellationPenalty.value)
        - this.getNumber(this.taxOnCancellationPenalty.value)
        - this.getNumber(this.miscellaneousFee.value)
        - this.getNumber(this.taxOnMiscellaneousFee.value);
    }
    this.refundToPassenger.setValue(result.toFixed(this.decimals), { emitEvent: false });
  }

  getNumber(value) {
    return Number(value);
  }

  newTaxes(type, amount): FormGroup {
    return new FormGroup({
      type: new FormControl(type),
      amount: new FormControl(amount)
    });
  }

  removeTaxes(position) {
    this.taxMiscellaneousFees.removeAt(position);
    this.updateTotalTaxes();
  }

  getTaxMiscellaneousFees(position = null) {
    return position == null ?
      this.taxMiscellaneousFees.controls :
      this.taxMiscellaneousFees.controls[position];
  }

  setSubscriptions() {
    this.suscrPartialRefund = this.partialRefund.valueChanges.subscribe(() => {
      if (this.amountRefundGroup.get('partialRefund').value) {
        this.lessGrossFareUsed.enable();
        this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), { emitEvent: true });
      } else {
        this.lessGrossFareUsed.disable();
        this.lessGrossFareUsed.setValue(this.setValueAmounts());
      }
    });

    this.suscrGrossFare = this.grossFare.valueChanges.subscribe(() => {
      this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), { emitEvent: true });
      this.updateRefundToPassenger();
    });

    this.suscriptionFormGeneral = this.lessGrossFareUsed.valueChanges.subscribe(() => {
      this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), { emitEvent: true });
      this.updateRefundToPassenger();
    });

    this.suscrRadio = this.radioCommission.valueChanges.subscribe(() => {
      if (this.radioCommission.value == 'true') {
        setTimeout(() => {
          this.commissionRate.setValue(this.setValueAmounts());
        }, 0);
        this.commissionRate.disable();
        this.commissionRate.reset();
        this.commissionAmount.enable();
      } else {
        setTimeout(() => {
          this.commissionAmount.setValue(this.setValueAmounts());
        }, 0);
        this.commissionAmount.disable();
        this.commissionRate.enable();
        this.commissionRate.setValue('0');
      }

    });

    this.suscrRadioCpMp = this.radioCpAndMfCommission.valueChanges.subscribe(() => {

      if (this.radioCpAndMfCommission.value == 'true') {
        setTimeout(() => {
          this.commissionOnCpAndMfRate.setValue(this.setValueAmounts());
        }, 0);
        this.commissionOnCpAndMfRate.disable();
        this.commissionOnCpAndMfRate.reset();
        this.commissionOnCpAndMfAmount.enable();
      } else {
        setTimeout(() => {
          this.commissionOnCpAndMfAmount.setValue(this.setValueAmounts());
        }, 0);
        this.commissionOnCpAndMfAmount.disable();
        this.commissionOnCpAndMfRate.enable();
        this.commissionOnCpAndMfRate.setValue('0');
      }

    });

    this.suscrNetRemit = this.netRemit.valueChanges.subscribe(() => {

      if (this.netRemit.value) {
        this.dicountAmount.enable();
      } else {
        this.dicountAmount.disable();
        this.dicountAmount.setValue(this.setValueAmounts());
      }
    });

    this.suscriptionFormGeneral = this.amountRefundGroup.valueChanges.subscribe(() => {
      this.updateRefundToPassenger();
    });
  }

  removeSubscriptions() {
    this.suscriptionFormGeneral.unsubscribe();
    this.suscrNetRemit.unsubscribe();
    this.suscrRadio.unsubscribe();
    this.suscrRadioCpMp.unsubscribe();
    this.suscrPartialRefund.unsubscribe();
    this.suscrGrossFare.unsubscribe();
    this.suscrLessGrossFare.unsubscribe();
  }


}
