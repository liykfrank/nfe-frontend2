import { AmountRefundFormModel } from './../../models/amount-refund-form.model';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { Component, EventEmitter, OnDestroy, Output, Input, OnInit } from '@angular/core';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';

@Component({
  selector: 'bspl-amount-refund',
  templateUrl: './amount-refund.component.html',
  styleUrls: ['./amount-refund.component.scss']
})
export class AmountRefundComponent extends ReactiveFormHandler implements OnInit {

  private refundConfiguration: RefundConfiguration;
  private amountRefundFormModel: AmountRefundFormModel = new AmountRefundFormModel();
  private countCuponsState: number;
  private _currencyState: Currency;

  errorPartialRefund = false;


  @Output() returnAmount = new EventEmitter();

  constructor(private _refundConfigurationService: RefundConfigurationService, private _currencyService: CurrencyService) {
    super();
  }

  ngOnInit(): void {
    this._refundConfigurationService.getConfiguration().subscribe(config => {
      this.refundConfiguration = config;
      this._refreshPartialRefund(this.countCuponsState, config.maxCouponsInRelatedDocuments);
    });

    this._refundConfigurationService.getCountCuponsState().subscribe(count => {
      this.countCuponsState = count;
      this._refreshPartialRefund(count, this.refundConfiguration.maxCouponsInRelatedDocuments);
    });

    this._currencyService.getCurrencyState().subscribe(currency => {
      this._currencyState = currency;
      this.amountRefundFormModel.decimals = this._currencyState.numDecimals;
    });

    this.subscribe(this.amountRefundFormModel.amountRefundGroup);

    this.subscriptions.push(
      this.amountRefundFormModel.taxMiscellaneousFees.valueChanges.subscribe(() => this.amountRefundFormModel.updatetax())
    );
  }



  getNumDecimals() {
    if (this._currencyState) {
      return this._currencyState.numDecimals;
    }
  }

  private _refreshPartialRefund(countCuponsSelected: number, maxCouponsInRelatedDocuments: number) {
    if (countCuponsSelected > maxCouponsInRelatedDocuments) {
      this.amountRefundFormModel.partialRefund.disable();
      this.errorPartialRefund = true;
    } else {
      this.amountRefundFormModel.partialRefund.enable();
      this.errorPartialRefund = false;
    }
  }


}
