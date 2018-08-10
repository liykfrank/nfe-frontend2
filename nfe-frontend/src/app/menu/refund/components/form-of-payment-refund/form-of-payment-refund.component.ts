import { log } from 'util';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { TranslationService } from 'angular-l10n';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { FormOfPaymentRefundFormModel } from '../../models/form-of-payment-refund-form.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/enums/alert-type.enum';

@Component({
  selector: 'bspl-form-of-payment-refund',
  templateUrl: './form-of-payment-refund.component.html',
  styleUrls: ['./form-of-payment-refund.component.scss']
})
export class FormOfPaymentRefundComponent extends ReactiveFormHandler
  implements OnInit {

  refundConfiguration: RefundConfiguration;
  formOfPaymentRefundFormModel: FormOfPaymentRefundFormModel = new FormOfPaymentRefundFormModel();
  type = EnvironmentType.REFUND_INDIRECT;


  // FOP_MSCA_VALUE: string = 'msca';
  // FOP_MSCC_VALUE: string = 'mscc';
  // FOP_CC_VALUE: string = 'cc'; // credit card
  // FOP_CA_VALUE: string = 'ca'; // cash


  @ViewChild('fopType') fopType: ElementRef;

  constructor(
    private translationService: TranslationService,
    private refundConfigurationService: RefundConfigurationService,
    private _alertsService: AlertsService
  ) {
    super();
    this.subscriptions.push(this.refundConfigurationService
      .getConfiguration()
      .subscribe(config => (this.refundConfiguration = config)));
    this.subscribe(this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup);
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.formOfPaymentRefundFormModel.formOfPaymentAmounts.valueChanges.subscribe(
        (value) => {
          this.updateTotals();
        }
      )
    );

    this.subscriptions.push(
      this.formOfPaymentRefundFormModel.type.valueChanges.subscribe(
        (value) => {

        }
      )
    );
  }

  onReturnFormCurrency(event) {
    this.formOfPaymentRefundFormModel.code.setValue(event.get('code').value);
    this.formOfPaymentRefundFormModel.decimals.setValue(event.get('decimals').value);
  }

  fopAmountAdditionIsPossible(fopTypeText: string): boolean {
    const creditCardText = this.sanitize(
      this.translationService.translate('REFUNDS.FORM_OF_PAYMENT.creditCard')
    );
    const msccText = this.sanitize(
      this.translationService.translate('REFUNDS.FORM_OF_PAYMENT.mscc')
    );
    const fopTypeCC: boolean =
      fopTypeText == creditCardText;

    const fopTypeAmountsNumber = this.formOfPaymentRefundFormModel.formOfPaymentAmounts.controls.filter(
      elem => {
        return this.sanitize(elem.value.type) == fopTypeText;
      }
    ).length;

    return (
      (fopTypeCC && fopTypeAmountsNumber <= 1) ||
      (!fopTypeCC && fopTypeAmountsNumber == 0)
    );
  }

  showCustomerRef(): boolean {
    const creditCardText = this.sanitize(
      this.translationService.translate('REFUNDS.FORM_OF_PAYMENT.creditCard')
    );
    const msccText = this.sanitize(
      this.translationService.translate('REFUNDS.FORM_OF_PAYMENT.mscc')
    );

    const fopTypeAmountsNumber = this.formOfPaymentRefundFormModel.formOfPaymentAmounts.controls.filter(
      elem => {
        return (
          this.sanitize(elem.value.type) == creditCardText ||
          this.sanitize(elem.value.type) == msccText
        );
      }
    ).length;

    return fopTypeAmountsNumber > 0 ? true : false;
  }

  sanitize(text: string): string {
    return text.toLowerCase().replace(' ', '');
  }

  addFopAmount(): void {
    const formOfPaymentTypeValue: string = this.formOfPaymentRefundFormModel
      .type.value;
    const formOfPaymentTypeText: string = this.fopType.nativeElement
      .selectedOptions[0].textContent;
    const amount: number = +this.formOfPaymentRefundFormModel.amount.value;

    if (
      this.fopAmountAdditionIsPossible(this.sanitize(formOfPaymentTypeText))
    ) {
      // msca and cash
      if (
        formOfPaymentTypeValue == 'msca' ||
        formOfPaymentTypeValue == 'cash'
      ) {
        this.formOfPaymentRefundFormModel.addCashAmount(
          amount.toFixed(2),
          formOfPaymentTypeText
        );

        // credic card, mscc, easy pay
      } else if (formOfPaymentTypeValue != '') {
        this.formOfPaymentRefundFormModel.addCreditOrEPAmount(
          amount.toFixed(2),
          formOfPaymentTypeText
        );
      }
    } else { // TODO: crear etiquetas
      this._alertsService.setAlertTranslate('REFUNDS.FORM_OF_PAYMENT.addFop.error.title', 'REFUNDS.FORM_OF_PAYMENT.addFop.error.title', AlertType.WARN);
    }
  }

  deleteAmount(i: number) {
    this.formOfPaymentRefundFormModel.deleteAmountFormArrayElem(i);
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
      creditEPSubTotal.toFixed(2)
    );
    this.formOfPaymentRefundFormModel.totalAmount.setValue(
      totalRefundAmount.toFixed(2)
    );

    this.formOfPaymentRefundFormModel.amount.setValue(null);
    this.formOfPaymentRefundFormModel.type.setValue('');
  }

  // isFopTypeCcOrMscc(): boolean {
  //   return this.formOfPaymentRefundFormModel.type ==
  // }

}
