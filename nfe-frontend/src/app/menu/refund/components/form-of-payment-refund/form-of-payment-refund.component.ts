import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';
import { GLOBALS } from '../../../../shared/constants/globals';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { FormOfPaymentRefundFormModel } from '../../models/form-of-payment-refund-form.model';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';

@Component({
  selector: 'bspl-form-of-payment-refund',
  templateUrl: './form-of-payment-refund.component.html',
  styleUrls: ['./form-of-payment-refund.component.scss']
})
export class FormOfPaymentRefundComponent
  extends ReactiveFormHandler<FormOfPaymentRefundFormModel>
  implements OnInit {
  refundConfiguration: RefundConfiguration;

  formOfPaymentRefundGroup: FormGroup;
  selectAmount: FormGroup;

  type = EnvironmentType.REFUND_INDIRECT;

  validFopSelected: boolean = true;
  fopList = GLOBALS.FORM_OF_PAYMENT;

  PT_CAPITALTEXT = GLOBALS.HTML_PATTERN.CAPITALTEXT;
  PT_NUMBER = GLOBALS.HTML_PATTERN.NUMERIC;
  PT_ALPHANUMERIC = GLOBALS.HTML_PATTERN.ALPHANUMERIC_UPPERCASE;
  PT_ALPHANUMERIC_LOWERCASE_WITH_SPACE = GLOBALS.HTML_PATTERN.ALPHANUMERIC_LOWERCASE_WITH_SPACE;
  PT_TOUR_CODE = GLOBALS.HTML_PATTERN.TOUR_CODE;

  private _currencyState: Currency;

  constructor(
    private refundConfigurationService: RefundConfigurationService,
    private _currencyService: CurrencyService
  ) {
    super();
  }

  ngOnInit(): void {
    this.formOfPaymentRefundGroup = this.model.formOfPaymentRefundGroup;
    this.selectAmount = this.model.selectAmount;

    this.subscriptions.push(
      this.refundConfigurationService.getConfiguration().subscribe(config => {
        this.refundConfiguration = config;

        if (config.easyPayEnabled) {
          if (this.fopList.indexOf(GLOBALS.FORM_OF_PAYMENT_EP) == -1) {
            this.fopList.push(GLOBALS.FORM_OF_PAYMENT_EP);
          }
        } else {
          if (this.fopList.indexOf(GLOBALS.FORM_OF_PAYMENT_EP) != -1) {
            this.fopList.pop();
          }
        }
      })
    );

    this.subscriptions.push(
      this._currencyService.getCurrencyState().subscribe(currency => {
        this._currencyState = currency;
      })
    );

    this.subscriptions.push(
      this.model.formOfPaymentAmounts.valueChanges.subscribe(() =>
        this._updateTotals()
      )
    );

    this.subscriptions.push(
      this.model.type.valueChanges.subscribe(val => {
        if (
          val == GLOBALS.FORM_OF_PAYMENT[1] ||
          val == GLOBALS.FORM_OF_PAYMENT[3]
        ) {
          this.selectAmount.get('number').enable();
          this.selectAmount.get('vendorCode').enable();
        } else {
          this.selectAmount.get('number').disable();
          this.selectAmount.get('vendorCode').disable();
          this.selectAmount.get('number').reset();
          this.selectAmount.get('vendorCode').reset();
        }

        const lst = this.model.formOfPaymentAmounts.value.filter(
          x => x.type == val
        );

        this.validFopSelected =
          lst.length < (val == GLOBALS.FORM_OF_PAYMENT[1] ? 2 : 1);
      })
    );
  }

  getNumDecimals() {
    if (this._currencyState) {
      return this._currencyState.numDecimals;
    }

    return 0;
  }

  getFormOfPaymentAmounts(): FormArray {
    return this.model.formOfPaymentAmounts;
  }

  showCustomerRef(): boolean {
    const lst = this.model.formOfPaymentAmounts.value.filter(
      x =>
        x.type == GLOBALS.FORM_OF_PAYMENT[1] ||
        x.type == GLOBALS.FORM_OF_PAYMENT[3]
    );
    return lst.length > 0;
  }

  disabledPlusIcon(): boolean {
    return !this.model.type.value || !this.model.selectAmount.valid;
  }

  onClickPlusIcon(): void {
    const vendorCode = this.selectAmount.get('vendorCode').enabled
      ? this.selectAmount.get('vendorCode').value
      : null;
    const numberCC = this.selectAmount.get('number').enabled
      ? this.selectAmount.get('number').value
      : null;

    this.model.formOfPaymentAmounts.push(
      this._getFOP(
        this.model.type.value,
        this.selectAmount.get('amount').value,
        vendorCode,
        numberCC
      )
    );
    this.model.type.reset();
    this.selectAmount.reset();
    this.selectAmount.get('amount').setValue(0);
  }

  getLabelType(pos: number): string {
    return `REFUNDS.FORM_OF_PAYMENT.${
      this.model.formOfPaymentAmounts.get(pos.toString()).get('type').value
    }`;
  }

  onCkickTrashIcon(i: number) {
    this.model.formOfPaymentAmounts.removeAt(i);
  }

  private _updateTotals(): void {
    let creditEPSubTotal = 0;
    let totalRefundAmount = 0;

    for (const aux of this.model.formOfPaymentAmounts.controls) {
      const val = aux.get('amount').value;

      if (aux.get('vendorCode').enabled && aux.get('number').enabled) {
        creditEPSubTotal += val;
      }

      totalRefundAmount += val;
    }

    this.model.creditEPSubTotal.setValue(creditEPSubTotal);
    this.model.totalAmount.setValue(totalRefundAmount);
  }

  private _getFOP(type: string, amount: string, vendorCode?: string, numberCC?: number): FormGroup {
    return new FormGroup({
      type: new FormControl(type),
      amount: new FormControl(amount),
      vendorCode: new FormControl(this._getFormStat(vendorCode), [Validators.required]),
      number: new FormControl(this._getFormStat(numberCC), [Validators.required])
    });
  }

  private _getFormStat(val) {
    return { value: val, disabled: val ? false : true }
  }
}
