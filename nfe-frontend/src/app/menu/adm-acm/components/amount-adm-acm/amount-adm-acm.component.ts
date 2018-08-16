import { UtilsService } from './../../../../shared/services/utils.service';
import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { CurrencyPost } from '../../../../shared/components/currency/models/currency-post.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { InputAmountServer } from '../../models/input-amount-server.model';
import { AcdmConfigurationService } from '../../services/adm-acm-configuration.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AcdmAmountForm } from './../../models/acdm-amount-form.model';

@Component({
  selector: 'bspl-amount-adm-acm',
  templateUrl: './amount-adm-acm.component.html',
  styleUrls: ['./amount-adm-acm.component.scss']
})
export class AmountAdmAcmComponent extends ReactiveFormHandler
  implements OnInit {
  private acdmAmountModel = new AcdmAmountForm();
  amountForm: FormGroup = this.acdmAmountModel._amountModelGroup;

  agentCalcFormGroup: FormGroup = (this.amountForm.get('agentCalculations') as FormGroup);
  airlineCalcFormGroup: FormGroup = (this.amountForm.get('airlineCalculations') as FormGroup);

  configuration: AdmAcmConfiguration;

  @Input()
  isAdm: boolean;

  subType: string;
  currency: CurrencyPost;
  showSpam: boolean;
  concernsIndicator: string;

  totalInputAmount: InputAmountServer = new InputAmountServer();
  totalAgent: number = 0;
  totalAirline: number = 0;
  total: number = 0;

  private isSimpleView: boolean;
  private taxOnCommissionSign: number; // sign on TOCA (1/-1)
  private agentInputAmount: InputAmountServer = new InputAmountServer();
  private airlineInputAmount: InputAmountServer = new InputAmountServer();

  constructor(
    private _acdmConfigurationService: AcdmConfigurationService,
    private _basicInfoService: BasicInfoService,
    private _utils: UtilsService
  ) {
    super();

    this.subscriptions.push(
      this._acdmConfigurationService.getConfiguration().subscribe(data => {
        this.configuration = data;
        this.amountForm.reset();
      })
    );

    this.subscribe(this.amountForm);
  }

  ngOnInit() {
    this.subscriptions.push(
      this.amountForm.get('agentCalculations').valueChanges.subscribe(value => {
        this._setOnModel('agentCalculations', this.agentInputAmount);
        this._setOnModel('airlineCalculations', this.airlineInputAmount);
        this._setTotals();
      })
    );

    this.subscriptions.push(
      this.amountForm
        .get('airlineCalculations')
        .valueChanges.subscribe(value => {
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
      })
    );

    this.subscriptions.push(
      this._basicInfoService.getSubType().subscribe(subtype => {
        this.subType = subtype;

        const list: string[] = ['SPDR', 'SPCR', 'ACMA', 'ADMA'];
        this.isSimpleView = list.indexOf(this.subType) >= 0;

        this.amountForm.get('agentCalculations').reset();
        this.amountForm.get('airlineCalculations').reset();
      })
    );

    this.subscriptions.push(
      this._basicInfoService.getCurrency().subscribe(currency => {
        this.currency = currency;
      })
    );

    this.subscriptions.push(
      (this.amountForm
        .get('taxMiscellaneousFees') as FormArray)
        .valueChanges.subscribe(value => {
          this._validTaxes();
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
    const taxPos = this.amountForm.controls['taxMiscellaneousFees']['controls'][
      pos
    ];

    if ((taxPos.get(key).dirty || taxPos.get(key).touched) && taxPos.get(key).errors ) {
      return true;
    }

    return false;
  }

  getFormArray(): FormArray {
    return this.amountForm.controls['taxMiscellaneousFees']['controls'];
  }

  getTaxValue(pos: number): number {
    const agent = this.amountForm
      .get('taxMiscellaneousFees')
      .get(pos.toString())
      .get('agentAmount').value;
    const airline = this.amountForm
      .get('taxMiscellaneousFees')
      .get(pos.toString())
      .get('airlineAmount').value;
    const direction = this._checkSide();

    return direction * agent + -1 * direction * airline;
  }

  removeTax(pos: number) {
    const arr = this.amountForm.controls['taxMiscellaneousFees'][
      'controls'
    ].length;

    if (arr == 1) {
      this.amountForm.get('taxMiscellaneousFees').reset();
    } else {
      this.acdmAmountModel.remove(pos);
    }
  }

  addTax() {
    if (this._validTaxes()) {
      this.acdmAmountModel.addTax();
    }
  }

  clean() {
    this.amountForm.get('taxMiscellaneousFees').reset();
    this.acdmAmountModel.addTax();
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
      case 'I':
      case 'X':
      case 'E':
        aux = this.isAdm ? -1 : 1;
        break;
      case 'R':
        aux = this.isAdm ? 1 : -1;
        break;
    }
    return aux;
  }

  private _validTaxes(): boolean {
    let ret = true;

    for (const aux of this.amountForm.controls['taxMiscellaneousFees'][
      'controls'
    ]) {
      const value = aux.get('type').value;

      if (aux.get('type').valid && value && value.length > 0) {
        if (
          !this.configuration.cpPermittedForConcerningIssue &&
          this.concernsIndicator == 'I' &&
          value == 'CP'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.cpPermittedForConcerningRefund &&
          this.concernsIndicator == 'R' &&
          value == 'CP'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.mfPermittedForConcerningIssue &&
          this.concernsIndicator == 'I' &&
          value == 'MF'
        ) {
          aux.get('type').setErrors(this._setCustomError('NOT_VALID'));
        } else if (
          !this.configuration.mfPermittedForConcerningIssue &&
          this.concernsIndicator == 'R' &&
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
