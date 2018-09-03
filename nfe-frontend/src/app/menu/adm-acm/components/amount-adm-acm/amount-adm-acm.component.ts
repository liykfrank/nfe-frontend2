import { AmountRefundFormModel } from './../../../refund/models/amount-refund-form.model';
import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormGroup, FormControl, Validators } from '@angular/forms';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { CurrencyPost } from '../../../../shared/components/currency/models/currency-post.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { InputAmountServer } from '../../models/input-amount-server.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AcdmAmountForm } from './../../models/acdm-amount-form.model';
import { GLOBALS } from '../../../../shared/constants/globals';

@Component({
  selector: 'bspl-amount-adm-acm',
  templateUrl: './amount-adm-acm.component.html',
  styleUrls: ['./amount-adm-acm.component.scss']
})
export class AmountAdmAcmComponent extends ReactiveFormHandler<AcdmAmountForm> implements OnInit {

  @Input() isAdm: boolean;

  amountForm: FormGroup;

  agentCalcFormGroup: FormGroup;
  airlineCalcFormGroup: FormGroup;

  configuration: AdmAcmConfiguration;

  subType: string;
  currency: CurrencyPost;
  showSpam: boolean;
  showToca: boolean;
  concernsIndicator: string;

  totalInputAmount: InputAmountServer = new InputAmountServer();
  totalAgent: number = 0;
  totalAirline: number = 0;
  total: number = 0;

  PT_TAX = GLOBALS.HTML_PATTERN.ALPHANUMERIC_UPPERCASE;

  private isSimpleView: boolean;
  private agentInputAmount: InputAmountServer = new InputAmountServer();
  private airlineInputAmount: InputAmountServer = new InputAmountServer();

  constructor(
    private _acdmConfigurationService: AcdmConfigurationService,
    private _basicInfoService: BasicInfoService
  ) {
    super();
  }

  ngOnInit() {
    this.amountForm = this.model.amountModelGroup;
    this.agentCalcFormGroup = this.model.agentCalculations;
    this.airlineCalcFormGroup = this.model.airlineCalculations;

    this.subscriptions.push(
      this._acdmConfigurationService.getConfiguration().subscribe(data => {
        this.configuration = data;
        this.amountForm.reset();
      })
    );

    this.subscriptions.push(
      this.agentCalcFormGroup.valueChanges.subscribe(value => {
        this._setOnModel('agentCalculations', this.agentInputAmount);
        this._setOnModel('airlineCalculations', this.airlineInputAmount);
        this._setTotals();
      })
    );

    this.subscriptions.push(
      this.airlineCalcFormGroup.valueChanges.subscribe(value => {
          this._setOnModel('agentCalculations', this.agentInputAmount);
          this._setOnModel('airlineCalculations', this.airlineInputAmount);
          this._setTotals();
        })
    );

    this.subscriptions.push(
      this._basicInfoService
        .getConcernsIndicator()
        .subscribe(concernsIndicator => {
          this.concernsIndicator = concernsIndicator;
        })
    );

    this.subscriptions.push(
      this._basicInfoService.getShowSpam().subscribe(showSpam => {
        this.showSpam = showSpam;

        this.agentCalcFormGroup
          .get('spam')
          .setValue(0);
        this.airlineCalcFormGroup
          .get('spam')
          .setValue(0);
      })
    );

    this.subscriptions.push(
      this._basicInfoService.getSubType().subscribe(subtype => {
        this.subType = subtype;

        const list: string[] = GLOBALS.ACDM.COMPLEX_TYPE;
        this.isSimpleView = list.indexOf(this.subType) >= 0;

        this.agentCalcFormGroup.reset();
        this.airlineCalcFormGroup.reset();
      })
    );

    this.subscriptions.push(
      this._basicInfoService.getCurrency().subscribe(currency => {
        this.amountForm.reset();
        this.currency = currency;
      })
    );

    this.subscriptions.push(
      this.model.taxMiscellaneousFees.valueChanges.subscribe(value => {
        this._validTaxes();
      })
    );

    this.subscriptions.push(
      this._basicInfoService.getToca().subscribe(data => {
        if (data.length > 0) {
          this.showToca = true;
        } else {
          this.showToca = false;

          this.agentCalcFormGroup
            .get('taxOnCommission')
            .setValue(0);
          this.airlineCalcFormGroup
            .get('taxOnCommission')
            .setValue(0);
        }
      })
    );
  }

  showSimpleView(val: boolean) {
    return this.isSimpleView;
  }

  checkIfErrorExist(father: string, key: string) {
    if (this.amountForm.get(father).get(key).errors) {
      return true;
    }
    return false;
  }

  checkIfErrorOnTaxExist(father: string, pos: number, key: string) {
    const taxPos = this.model.taxMiscellaneousFees.controls[pos];

    if (
      (taxPos.get(key).dirty || taxPos.get(key).touched) &&
      taxPos.get(key).errors
    ) {
      return true;
    }

    return false;
  }

  getFormArray() {
    return this.model.taxMiscellaneousFees.controls;
  }

  getTaxValue(pos: number): number {
    const agent = this.model.taxMiscellaneousFees.controls[pos].get('agentAmount').value;
    const airline = this.model.taxMiscellaneousFees.controls[pos].get('airlineAmount').value;

    const direction = this._checkSide();

    return direction * agent + -1 * direction * airline;
  }

  removeTax(pos: number) {
    const arr = this.model.taxMiscellaneousFees.length;

    if (arr == 1) {
      this.model.taxMiscellaneousFees.reset();
    } else {
      this.model.taxMiscellaneousFees.removeAt(pos);
    }
  }

  addTax() {
    if (this._validTaxes()) {
      this.model.taxMiscellaneousFees.push(this.model._taxFormModelGroup());
    }
  }

  clean() {
    while (
      this.model.taxMiscellaneousFees.length > 0
    ) {
      this.model.taxMiscellaneousFees.removeAt(0);
    }

    this.model.taxMiscellaneousFees.push(this.model._taxFormModelGroup());
  }

  populate() {}

  private _setTotals() {
    this._setOnModel('agentCalculations', this.agentInputAmount);
    this._setOnModel('airlineCalculations', this.airlineInputAmount);

    this._calculateTotalOnInput();

    this.totalAgent = this._calculateTotal(this.agentInputAmount);
    this.totalAirline = this._calculateTotal(this.airlineInputAmount);
    this.total = this._calculateTotal(this.totalInputAmount);

    this.amountForm.get('amountPaidByCustomer').setValue(this.total);
  }

  private _calculateTotalOnInput() {
    const direction = this._checkSide();

    this.totalInputAmount.fare =
      direction * this.agentInputAmount.fare +
      -1 * direction * this.airlineInputAmount.fare;

    this.totalInputAmount.tax =
      direction * this.agentInputAmount.tax +
      -1 * direction * this.airlineInputAmount.tax;

    this.totalInputAmount.commission =
      direction * this.agentInputAmount.commission +
      -1 * direction * this.airlineInputAmount.commission;

    this.totalInputAmount.spam =
      direction * this.agentInputAmount.spam +
      -1 * direction * this.airlineInputAmount.spam;

    this.totalInputAmount.taxOnCommission =
      direction * this.agentInputAmount.taxOnCommission +
      -1 * direction * this.airlineInputAmount.taxOnCommission;
  }

  private _setOnModel(form: string, model: InputAmountServer) {
    model.commission = this.amountForm.get(form).get('commission').value;
    model.fare = this.amountForm.get(form).get('fare').value;
    model.spam = this.amountForm.get(form).get('spam').value;
    model.tax = this.amountForm.get(form).get('tax').value;
    model.taxOnCommission = this.amountForm
      .get(form)
      .get('taxOnCommission').value;
  }

  private _calculateTotal(elem: InputAmountServer): number {
    return (
      Number(elem.fare) +
      Number(elem.tax) -
      Number(elem.commission) -
      Number(elem.spam) +
      this.configuration.taxOnCommissionSign * Number(elem.taxOnCommission)
    );
  }

  private _checkSide() {
    let aux;
    switch (this.concernsIndicator) {
      case GLOBALS.ACDM.FOR_REFUND:
        aux = this.isAdm ? 1 : -1;
        break;
      default:
        aux = this.isAdm ? -1 : 1;
        break;
    }

    return aux;
  }

  private _validTaxes(): boolean {
    let ret = true;

    for (const aux of this.model.taxMiscellaneousFees.controls) {
      const value = aux.get('type').value;

      if (aux.get('type').valid && value && value.length > 0) {
        if (
          !this.configuration.cpPermittedForConcerningIssue &&
          this.concernsIndicator == GLOBALS.ACDM.FOR_ISSUE &&
          value == 'CP'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.cpPermittedForConcerningRefund &&
          this.concernsIndicator == GLOBALS.ACDM.FOR_REFUND &&
          value == 'CP'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.mfPermittedForConcerningIssue &&
          this.concernsIndicator == GLOBALS.ACDM.FOR_ISSUE &&
          value == 'MF'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.mfPermittedForConcerningIssue &&
          this.concernsIndicator == GLOBALS.ACDM.FOR_REFUND &&
          value == 'MF'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        }
      } else {
        aux.get('type').setErrors(this._setCustomError('NOT_EMPTY'));
        ret = false;
      }
    }

    return ret;
  }

  private _setCustomError(msg: string) {
    return {
      customError: {
        invalid: true,
        message: msg
      }
    };
  }

}
