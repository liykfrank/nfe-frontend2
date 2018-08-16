import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { TranslationService } from 'angular-l10n';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { GLOBALS } from '../../../../shared/constants/globals';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { FormOfPaymentRefundFormModel } from '../../models/form-of-payment-refund-form.model';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';
import { Currency } from '../../../../shared/components/currency/models/currency.model';

@Component({
  selector: 'bspl-form-of-payment-refund',
  templateUrl: './form-of-payment-refund.component.html',
  styleUrls: ['./form-of-payment-refund.component.scss']
})
export class FormOfPaymentRefundComponent extends ReactiveFormHandler
  implements OnInit {

  refundConfiguration: RefundConfiguration;
  formOfPaymentRefundFormModel: FormOfPaymentRefundFormModel = new FormOfPaymentRefundFormModel();
  selectAmount = this.formOfPaymentRefundFormModel.selectAmount;
  type = EnvironmentType.REFUND_INDIRECT;
  validFopSelected = true;

  FOP_MSCA_VALUE = GLOBALS.FORM_OF_PAYMENT.MSCA_VALUE;
  FOP_MSCC_VALUE = GLOBALS.FORM_OF_PAYMENT.MSCC_VALUE;
  FOP_CC_VALUE = GLOBALS.FORM_OF_PAYMENT.CC_VALUE; // credit card
  FOP_CA_VALUE = GLOBALS.FORM_OF_PAYMENT.CA_VALUE; // cash
  FOP_EP_VALUE = GLOBALS.FORM_OF_PAYMENT.EP_VALUE; // easy pay

  FOP_MSCC_TEXT = this.translationService.translate(
    'REFUNDS.FORM_OF_PAYMENT.mscc'
  );
  FOP_CC_TEXT = this.translationService.translate(
    'REFUNDS.FORM_OF_PAYMENT.paymentCard'
  ); // credit card

  @ViewChild('fopType')
  fopType: ElementRef;

  private _currencyState: Currency;

  constructor(
    private translationService: TranslationService,
    private refundConfigurationService: RefundConfigurationService,
    private _alertsService: AlertsService,
    private _currencyService: CurrencyService
  ) {
    super();

    this.refundConfigurationService.getConfiguration().subscribe(config => (this.refundConfiguration = config));
    this._currencyService.getCurrencyState().subscribe(currency => {this._currencyState = currency; });
    this.subscribe(this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup);
    this._currencyService.getCurrencyState().subscribe(currency => {
      this._currencyState = currency;
    });
  }

  ngOnInit(): void {}

  getNumDecimals() {
    if (this._currencyState) {
      return this._currencyState.numDecimals;
    }
  }

  isFopTypeSelectedCaMscaOrVoid(): boolean {
    return (
      this.formOfPaymentRefundFormModel.type.value == this.FOP_CA_VALUE ||
      this.formOfPaymentRefundFormModel.type.value == this.FOP_MSCA_VALUE ||
      this.formOfPaymentRefundFormModel.type.value == ''
    );
  }

  isFopTypeTextMsccOrCc(fopTypeText: string): boolean {
    return fopTypeText == this.FOP_MSCC_TEXT || fopTypeText == this.FOP_CC_TEXT;
  }

  isFopTypeValueMscaOrCash(fopTypeValue: string): boolean {
    return (
      fopTypeValue == this.FOP_MSCA_VALUE || fopTypeValue == this.FOP_CA_VALUE
    );
  }

  showCustomerRef(): boolean {
    const fopTypeAmountsNumber = this.formOfPaymentRefundFormModel.formOfPaymentAmounts.controls.filter(
      elem => {
        return (
          this.isFopTypeTextMsccOrCc(elem.value.type) ||
          this.isFopTypeTextMsccOrCc(elem.value.type)
        );
      }
    ).length;
    return fopTypeAmountsNumber > 0 ? true : false;
  }

  onClickPlusIcon(): void {
    if (this.addFopAmount()) {
      this.updateTotals();
      this.formOfPaymentRefundFormModel.selectAmount.reset();
      this.formOfPaymentRefundFormModel.type.setValue('');
    }
  }

  fopAmountAdditionIsPossible(fopTypeText: string): boolean {
    const fopTypeCC: boolean = fopTypeText == this.FOP_CC_TEXT;

    const fopTypeAmountsNumber = this.formOfPaymentRefundFormModel.formOfPaymentAmounts.controls.filter(
      elem => {
        return elem.value.type == fopTypeText;
      }
    ).length;

    return (
      (fopTypeCC && fopTypeAmountsNumber <= 1) ||
      (!fopTypeCC && fopTypeAmountsNumber == 0)
    );
  }

  addFopAmount(): boolean {

    const formOfPaymentTypeValue: string = this.formOfPaymentRefundFormModel
      .type.value;
    // const formOfPaymentTypeText: string = this.fopType.nativeElement
    //   .selectedOptions[0].textContent;
    const amount: string = this.formOfPaymentRefundFormModel.selectAmount.get(
      'amount'
    ).value;
    const vendorCode: string = this.formOfPaymentRefundFormModel.selectAmount.get(
      'vendorCode'
    ).value;
    const number: number = this.formOfPaymentRefundFormModel.selectAmount.get(
      'number'
    ).value;

    if (this.fopAmountAdditionIsPossible(formOfPaymentTypeValue)) {
      // msca and cash
      if (
        this.isFopTypeValueMscaOrCash(formOfPaymentTypeValue) ||
        this.isFopTypeTextMsccOrCc(formOfPaymentTypeValue)
      ) {
        this.formOfPaymentRefundFormModel.addCashAmount(
          amount,
          formOfPaymentTypeValue
        );

        // credic card, mscc, easy pay
      } else if (formOfPaymentTypeValue != '') {
        if (this.formOfPaymentRefundFormModel.selectAmount.get('vendorCode').invalid ||
          this.formOfPaymentRefundFormModel.selectAmount.get('number').invalid) {
          this.formOfPaymentRefundFormModel.selectAmount.get('vendorCode').markAsDirty();
          this.formOfPaymentRefundFormModel.selectAmount.get('number').markAsDirty();
         return false;
       }
        this.formOfPaymentRefundFormModel.addCreditOrEPAmount(
          amount,
          formOfPaymentTypeValue,
          vendorCode,
          number
        );
      }

      return true;
    }
    this.validFopSelected = false;
    return false;
  }

  onCkickTrashIcon(i: number) {
    this.validFopSelected = true;
    this.formOfPaymentRefundFormModel.deleteAmountFormArrayElem(i);
    this.updateTotals();
  }

  updateTotals(): void {
    let iVendorCodeControl: FormControl;
    let iNumberControl: FormControl;
    let iAmountValue: number;
    let creditEPSubTotal = 0;
    let totalRefundAmount = 0;

    for (
      let i = 0;
      i <
      this.formOfPaymentRefundFormModel.formOfPaymentAmounts.controls.length;
      i++
    ) {
      iVendorCodeControl = this.formOfPaymentRefundFormModel
        .formOfPaymentAmounts.controls[i]['controls'].vendorCode;
      iNumberControl = this.formOfPaymentRefundFormModel.formOfPaymentAmounts
        .controls[i]['controls'].number;
      iAmountValue = +this.formOfPaymentRefundFormModel.formOfPaymentAmounts
        .controls[i]['controls'].amount.value;

      if (iVendorCodeControl && iNumberControl) {
        creditEPSubTotal += iAmountValue;
      }

      totalRefundAmount += iAmountValue;
    }

    this.formOfPaymentRefundFormModel.creditEPSubTotal.setValue(
      creditEPSubTotal
    );
    this.formOfPaymentRefundFormModel.totalAmount.setValue(totalRefundAmount);
  }
}
