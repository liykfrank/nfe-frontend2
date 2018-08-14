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

  private decimals = 0;


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
    return this.grossFare.value - this.lessGrossFareUsed.value;
  }

  createFormControls(): void {
    this.partialRefund = new FormControl('', [Validators.required]);
    this.netRemit = new FormControl('0', [Validators.required]);
    this.grossFare = new FormControl({ value: '0', disabled: this.partialRefund.value }, []);
    this.lessGrossFareUsed = new FormControl({ value: '0', disabled: this.partialRefund.value }, []);
    this.totalGrossFareRefunded = new FormControl({ value: '0', disabled: true }, []);
    this.totalTaxes = new FormControl({ value: '0', disabled: true }, []);
    this.radioCommission = new FormControl('', []);
    this.radioCpAndMfCommission = new FormControl('', []);
    // TODO Total gros fare refunded?
    this.commissionAmount = new FormControl({ value: '0', disabled: false }, []);
    this.commissionRate = new FormControl({ value: '', disabled: true }, []);
    this.dicountAmount = new FormControl({ value: '0', disabled: this.netRemit.value }, []);
    this.cancellationPenalty = new FormControl('0', []);
    this.taxOnCancellationPenalty = new FormControl('', []);
    this.miscellaneousFee = new FormControl('', []);
    this.taxOnMiscellaneousFee = new FormControl('', []);
    this.commissionOnCpAndMfAmount = new FormControl({ value: '', disabled: false }, []);
    this.commissionOnCpAndMfRate = new FormControl({ value: '', disabled: true }, []);
    this.tax = new FormControl('', []);
    this.refundToPassenger = new FormControl({value: '0', disabled: true}, []);
    this.amountType = new FormControl('', []);
    this.amountTax = new FormControl('0', []);
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
  }

  addTaxes(decimals: number) {
    this.decimals = decimals;

    this.taxMiscellaneousFees.push(this.newTaxes(this.amountType.value, this.amountTax.value));

    setTimeout(() => {
      this.amountType.reset();
      this.amountTax.reset();
    }, 0);
    this.updateTotalTaxes();
  }

  updateTotalTaxes() {
    let total = 0;
    for (const tax of this.taxMiscellaneousFees.controls) {
      total = total + Number(tax.value.amount);
    }
    this.totalTaxes.setValue(total.toFixed(this.decimals), {emitEvent: true});
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
    this.refundToPassenger.setValue(result, {emitEvent: false});
  }

  getNumber(value) {
    return Number(value);
  }

  newTaxes(type, amount): FormGroup {
    return new FormGroup({
      type: new FormControl(type, []),
      amount: new FormControl(amount, [])
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
        this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), {emitEvent: true});
      } else {
        this.lessGrossFareUsed.disable();
        this.lessGrossFareUsed.reset();
      }
    });

    this.suscrGrossFare = this.grossFare.valueChanges.subscribe(() => {
      this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), {emitEvent: true});
      this.updateRefundToPassenger();
    });

    this.suscriptionFormGeneral = this.lessGrossFareUsed.valueChanges.subscribe(() => {
      this.totalGrossFareRefunded.setValue(this.getValueForTotalGrossFareRefunded(), {emitEvent: true});
      this.updateRefundToPassenger();
    });

    this.suscrRadio = this.radioCommission.valueChanges.subscribe(() => {
      if (this.radioCommission.value == 'true') {
        setTimeout(() => {
          this.commissionRate.reset();
        }, 0);
        this.commissionRate.disable();
        this.commissionAmount.enable();
      } else {
        setTimeout(() => {
          this.commissionAmount.reset();
        }, 0);
        this.commissionAmount.disable();
        this.commissionRate.enable();
      }

    });

    this.suscrRadioCpMp = this.radioCpAndMfCommission.valueChanges.subscribe(() => {

      if (this.radioCpAndMfCommission.value == 'true') {
        setTimeout(() => {
          this.commissionOnCpAndMfRate.reset();
        }, 0);
        this.commissionOnCpAndMfRate.disable();
        this.commissionOnCpAndMfAmount.enable();
      } else {
        setTimeout(() => {
          this.commissionOnCpAndMfAmount.reset();
        }, 0);
        this.commissionOnCpAndMfAmount.disable();
        this.commissionOnCpAndMfRate.enable();
      }

    });

    this.suscrNetRemit = this.netRemit.valueChanges.subscribe(() => {
      if (this.netRemit.value) {
        this.dicountAmount.enable();
      } else {
        this.dicountAmount.disable();
        this.dicountAmount.reset();
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
