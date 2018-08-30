import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';
import { GLOBALS } from '../../../../shared/constants/globals';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { AmountRefundFormModel } from '../../models/amount-refund-form.model';

@Component({
  selector: 'bspl-amount-refund',
  templateUrl: './amount-refund.component.html',
  styleUrls: ['./amount-refund.component.scss']
})
export class AmountRefundComponent extends ReactiveFormHandler<AmountRefundFormModel> implements OnInit {

  refundConfiguration: RefundConfiguration;
  errorPartialRefund = false;

  amountRefundGroup: FormGroup;

  private countCuponsState: number;
  private _currencyState: Currency;

  PT_INPUT_PERCENT = GLOBALS.HTML_PATTERN.INPUT_PERCENT_MAX_99_99;
  PT_ALPHANUMERIC_UPPERCASE = GLOBALS.HTML_PATTERN.ALPHANUMERIC_UPPERCASE;

  constructor(
    private _refundConfigurationService: RefundConfigurationService,
    private _currencyService: CurrencyService
  ) {
    super();
  }

  ngOnInit(): void {
    this.amountRefundGroup = this.model.amountRefundGroup;

    this.subscriptions.push(
      this._refundConfigurationService.getConfiguration().subscribe(config => {
        this.refundConfiguration = config;
        this._refreshPartialRefund(
          this.countCuponsState,
          config.maxCouponsInRelatedDocuments
        );

        if (config.handlingFeeEnabled) {
          this.model.miscellaneousFee.enable();
          this.model.miscellaneousFee.setValue(config.mfAmount ? config.mfAmount : 0);
        } else {
          this.model.miscellaneousFee.reset();
          this.model.miscellaneousFee.disable();
        }

        if (config.penaltyChargeEnabled) {
          this.model.cancellationPenalty.enable();
        } else {
          this.model.cancellationPenalty.reset();
          this.model.cancellationPenalty.disable();
        }

        if (config.vatOnMfAndVatOnCpEnabled) {
          this.model.taxOnCancellationPenalty.enable();
          this.model.taxOnMiscellaneousFee.enable();
        } else {
          this.model.taxOnCancellationPenalty.disable();
          this.model.taxOnCancellationPenalty.reset();

          this.model.taxOnMiscellaneousFee.disable();
          this.model.taxOnMiscellaneousFee.reset();
        }
      })
    );

    this.subscriptions.push(
      this._refundConfigurationService
        .getCountCuponsState()
        .subscribe(count => {
          this.countCuponsState = count;
          this._refreshPartialRefund(
            count,
            this.refundConfiguration.maxCouponsInRelatedDocuments
          );
        })
    );

    this._currencyService.getCurrencyState().subscribe(currency => {
      this._currencyState = currency;
    });

    this.subscriptions.push(
      this.model.taxMiscellaneousFees.valueChanges.subscribe(() => this._updatetax())
    );

    this.subscriptions.push(
      this.model.partialRefund
        .valueChanges.subscribe(val => {
          if (val) {
            this.model.lessGrossFareUsed.enable();
            this.model.totalGrossFareRefunded
              .setValue(this._getValueForTotalGrossFareRefunded(), {
                emitEvent: true
              });
          } else {
            this.model.lessGrossFareUsed.disable();
            this.model.lessGrossFareUsed
              .setValue((0).toFixed(this.getNumDecimals()));
          }
        })
    );

    this.subscriptions.push(
      this.model.grossFare.valueChanges.subscribe(val => {
        this.model.totalGrossFareRefunded
          .setValue(this._getValueForTotalGrossFareRefunded(), {
            emitEvent: false
          });
        this._updateRefundToPassenger();
      })
    );

    this.subscriptions.push(
      this.model.lessGrossFareUsed.valueChanges.subscribe(val => {
        this.model.totalGrossFareRefunded
          .setValue(this._getValueForTotalGrossFareRefunded(), {
            emitEvent: true
          });
        this._updateRefundToPassenger();
      })
    );

    this.subscriptions.push(
      this.model.radioCommission.valueChanges.subscribe(val => {
        if (val == 'true') {
          this.model.commissionRate
            .setValue((0).toFixed(this.getNumDecimals()));

          this.model.commissionRate.disable();
          this.model.commissionRate.reset();
          this.model.commissionAmount.enable();
        } else {
          this.model.commissionAmount
            .setValue((0).toFixed(this.getNumDecimals()));

          this.model.commissionAmount.disable();
          this.model.commissionRate.reset();
          this.model.commissionRate.enable();
        }
      })
    );

    this.subscriptions.push(
      this.model.radioCpAndMfCommission.valueChanges.subscribe((val) => {
        if (val == 'true') {
          this.model.commissionOnCpAndMfRate
            .setValue((0).toFixed(this.getNumDecimals()));

          this.model.commissionOnCpAndMfRate.disable();
          this.model.commissionOnCpAndMfRate.reset();
          this.model.commissionOnCpAndMfAmount.enable();
        } else {
          this.model.commissionOnCpAndMfAmount
            .setValue((0).toFixed(this.getNumDecimals()));

          this.model.commissionOnCpAndMfAmount.disable();
          this.model.commissionOnCpAndMfRate.reset();
          this.model.commissionOnCpAndMfRate.enable();
        }
      })
    );

    this.subscriptions.push(
      this.model.netRemit.valueChanges.subscribe(val => {
        if (val) {
          this.model.spam.enable();
        } else {
          this.model.spam.disable();
          this.model.spam.setValue((0).toFixed(this.getNumDecimals()));
        }
      })
    );

    this.subscriptions.push(
      this.model.amounts.valueChanges.subscribe(() => this._updateRefundToPassenger())
    );
  }

  getTaxMiscellaneousFees() {
    return this.model.taxMiscellaneousFees;
  }

  getNumDecimals() {
    if (this._currencyState) {
      return this._currencyState.numDecimals;
    }

    return 0;
  }

  removeTax(pos: number) {
    this.getTaxMiscellaneousFees().removeAt(pos);
  }

  addTax() {
    this.getTaxMiscellaneousFees().push(
      new FormGroup({
        type: new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.TAX)]),
        amount: new FormControl(0)
      })
    );
  }

  showTrash(pos: number) {
    return pos < this.model.taxMiscellaneousFees.controls.length - 1;
  }

  disableAddTax() {
    return this.model.taxMiscellaneousFees.controls.filter(x => x.get('type').value == '').length > 0;
  }

  private _refreshPartialRefund(
    countCuponsSelected: number,
    maxCouponsInRelatedDocuments: number
  ) {
    if (countCuponsSelected > maxCouponsInRelatedDocuments) {
      this.model.partialRefund.disable();
      this.errorPartialRefund = true;
    } else {
      this.model.partialRefund.enable();
      this.errorPartialRefund = false;
    }
  }

  private _updatetax() {
    let total = 0;

    for (const tax of this.model.taxMiscellaneousFees.controls.filter(x => x.valid && x.get('type').value != '')) {
      total = total + Number(tax.get('amount').value);
    }

    this.model.tax
      .setValue(total, { emitEvent: false });
  }

  private _getValueForTotalGrossFareRefunded() {
    return (
      this.model.grossFare.value -
      this.model.lessGrossFareUsed.value
    ).toFixed(this.getNumDecimals());
  }

  private _updateRefundToPassenger() {
    let result = 0;

    if (this.model.partialRefund.value == 'true') {
      result = Number(this.model.grossFare.value);
    } else {
      result = Number(this.model.totalGrossFareRefunded.value);
    }

    result =
      result +
      Number(this.model.tax.value) -
      Number(this.model.cancellationPenalty.value) -
      Number(this.model.taxOnCancellationPenalty.value) -
      Number(this.model.miscellaneousFee.value) -
      Number(this.model.taxOnMiscellaneousFee.value);

    this.model.refundToPassenger
      .setValue(result, { emitEvent: false }); // ANTES .toFixed(this.getNumDecimals())
  }
}
