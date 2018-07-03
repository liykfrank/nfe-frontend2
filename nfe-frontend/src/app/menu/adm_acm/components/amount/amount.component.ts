import { AmountService } from './../../services/amount.service';
import { AdmAcmService } from './../../services/adm-acm.service';
import { Observable } from 'rxjs/Observable';
import { jqxDataTableComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatatable';


import { Component, OnInit, ViewChild, Injector, Input, EventEmitter } from '@angular/core';
import { NwAbstractComponent } from '../../../../shared/base/abstract-component';
import { InputAmount } from '../../models/inputAmount.model';
import { InputAmountServer } from '../../models/input-amount-server.model';
import { TaxAmountServer } from '../../models/tax-amount-server';
import { Configuration } from '../../models/configuration.model';

@Component({
  selector: 'app-amount',
  templateUrl: './amount.component.html',
  styleUrls: ['./amount.component.scss']
})
export class AmountComponent extends NwAbstractComponent implements OnInit {
  @Input() isADM: boolean;

  public totalAmount: InputAmount = this.getEmptyInput();
  public taxes: InputAmount[] = [];

  /* Data from service */
  decimals: number;
  typeSimpleView: boolean;
  total: number = 0;

  showSpam: boolean;
  showTOCA: boolean;

  private showCPTax: boolean;
  private showMFTax: boolean;

  private validTaxes: boolean; //check if (+)Taxes = Amount[tax]
  private taxOnCommissionSign: number; //sign on TOCA (1/-1)
  private conf: Configuration;
  /* Data from service */

  /* Data to service */
  agentCalculations: InputAmountServer = new InputAmountServer();
  airlineCalculations: InputAmountServer = new InputAmountServer();
  totalCalculations: InputAmountServer = new InputAmountServer();
  private taxMiscellaneousFees: TaxAmountServer[] = [];
  subType: String;
  private spdrType: String;
  private checkNR: boolean;
  /* Data to service */

  constructor(injector: Injector, private _AdmAcmService: AdmAcmService, private _AmountService: AmountService) {
    super(injector);

    this._AdmAcmService.getConfiguration().subscribe(value => {
      if (value != null) {
        this.conf = value;
        this.taxOnCommissionSign = value.taxOnCommissionSign;

        this.showCPTax = this.showCPTaxOnScreen();
        this.showMFTax = this.showMFTaxOnScreen();
        this.showSpam = this.showSpamOnScreen();
        this.showTOCA = this.showTOCAOnScreen();
      }
    });

    this._AdmAcmService.getDecimals().subscribe(decimals => {
      this.decimals = decimals;
    });

    this._AdmAcmService.getSpan().subscribe(value => {
        this.checkNR = value;
        this.showSpam = this.showSpamOnScreen();
    });

    this._AdmAcmService.getSubtype().subscribe(value => { //Type
      const list = ['ADMD', 'ACMD'];
      this.subType = value;
      this.typeSimpleView = list.indexOf(value) >= 0;

      this.agentCalculations = new InputAmountServer();
      this.airlineCalculations = new InputAmountServer();
      this.clean();

      this._AmountService.setAgentCalculations(this.agentCalculations);
      this._AmountService.setAirlineCalculations(this.airlineCalculations);
      this._AdmAcmService.setDecimals(0);
    });

    this._AdmAcmService.getSpdr().subscribe(value => {
      this.showCPTax = this.showCPTaxOnScreen();
      this.showMFTax = this.showMFTaxOnScreen();
    });

    this._AmountService.getTotal().subscribe(value => this.total = value);

    this._AmountService.getValidTaxes().subscribe(value => this.validTaxes = value);
  }

  ngOnInit() {
    this.taxes = [this.getEmptyInput()];
  }

  clean() {
    this.taxes = [this.getEmptyInput()];
    this._AmountService.setTaxMiscellaneousFees([]);
  }

  addTax() {
    const index = this.taxes.findIndex((v) => v.name.trim() == '');
    if (index < 0) {
      this.taxes.push(this.getEmptyInput());
    }
  }

  validateName(pos: number): void {

    if (!this.showCPTax && this.taxes[pos].name == 'CP') {
      this.taxes[pos].name = '';
    }

    if (!this.showMFTax && this.taxes[pos].name == 'MF') {
      this.taxes[pos].name = '';
    }
  }

  populate() {
    console.log('on populate');
  }

  simpleCalculateFare(event: any) {
    const total: number = event.args.owner.decimal;

    if (this.isADM) {
      this.airlineCalculations.fare = total;
      this._AmountService.setAirlineCalculations(this.airlineCalculations);
    } else {
      this.agentCalculations.fare = total;
      this._AmountService.setAgentCalculations(this.agentCalculations);
    }

    this._AmountService.setTotal(total);
  }

  calculateTotalOnAmount() {
    this.totalCalculations.commission =
      ((this.isADM ? 1 : -1) * this.agentCalculations.commission)
      + ((this.isADM ? 1 : -1) * this.airlineCalculations.commission);
    this.totalCalculations.fare =
    ((this.isADM ? 1 : -1) * this.agentCalculations.fare)
    + ((this.isADM ? 1 : -1) * this.airlineCalculations.fare);
    this.totalCalculations.spam =
      ((this.isADM ? 1 : -1) * this.agentCalculations.spam)
      + ((this.isADM ? 1 : -1) * this.airlineCalculations.spam);
    this.totalCalculations.tax =
      ((this.isADM ? 1 : -1) * this.agentCalculations.tax)
      + ((this.isADM ? 1 : -1) * this.airlineCalculations.tax);
    this.totalCalculations.taxOnCommission =
      ((this.isADM ? 1 : -1) * this.agentCalculations.taxOnCommission)
      + ((this.isADM ? 1 : -1) * this.airlineCalculations.taxOnCommission);

    this.totalAmount.agentValue = this.calculateTotal(this.agentCalculations);
    this.totalAmount.airlineValue = this.calculateTotal(this.airlineCalculations);
    this.totalAmount.dif = this.calculateTotal(this.totalCalculations);

    this._AmountService.setAgentCalculations(this.agentCalculations);
    this._AmountService.setAirlineCalculations(this.airlineCalculations);
    this._AmountService.setTotal(this.totalAmount.dif);
  }

  calculateOnTax() {
    this.taxMiscellaneousFees = [];

    for (let x of this.taxes) {
      if (x.name.trim() != '') {
        this.setDifOnRow(x);
        this.taxMiscellaneousFees.push({type: x.name, agentAmount: x.agentValue, airlineAmount: x.airlineValue});
      }
    }

    this._AmountService.setTaxMiscellaneousFees(this.taxMiscellaneousFees);
  }

  removeTax(num: number) {
    if (this.taxes.length > 1) {
      this.taxes.splice(num, 1);
    } else {
      this.taxes = [this.getEmptyInput()];
    }

    for (let x of this.taxes) {
      this.setDifOnRow(x);
      this.taxMiscellaneousFees.push({type: x.name, agentAmount: x.agentValue, airlineAmount: x.airlineValue});
    }

    this._AmountService.setTaxMiscellaneousFees(this.taxMiscellaneousFees);
  }

  private calculateTotal(elem: InputAmountServer): number {
    return Number(elem.fare) + Number(elem.tax) - Number(elem.commission) - Number(elem.spam)
      + (this.conf.taxOnCommissionSign * Number(elem.taxOnCommission));
  }

  private setDifOnRow(row) {
    row.dif =
      Number((Number(row.airlineValue)* (this.isADM ? 1 : -1))
      + (Number(row.agentValue) * (this.isADM ? -1 : 1))).toFixed(this.decimals);
  }

  private getEmptyInput(): InputAmount {
    return { name: '', agentValue: 0, airlineValue: 0, dif: 0 };
  }

  private showCPTaxOnScreen(): boolean {
    const ret = (this.conf.cpPermittedForConcerningIssue && this.spdrType == 'I') ||
                  (this.conf.cpPermittedForConcerningRefund && this.spdrType == 'R');

    if (!ret) {
      const aux: number = this.taxes.findIndex((x) => x.name.toUpperCase() == 'CP');
      if (aux >= 0) {
        this.removeTax(aux);
      }
    }

    return ret;
  }

  private showMFTaxOnScreen(): boolean{
    const ret = (this.conf.mfPermittedForConcerningIssue && this.spdrType == 'I') ||
                  (this.conf.mfPermittedForConcerningRefund && this.spdrType == 'R');

    if (!ret) {
      const aux: number = this.taxes.findIndex((x) => x.name.toUpperCase() == 'MF');
      if (aux >= 0) {
        this.removeTax(aux);
      }
    }

    return ret;
  }

  private showSpamOnScreen(): boolean {
    const ret = this.conf.nridAndSpamEnabled && this.checkNR;

    if (!ret) {
      this.agentCalculations.spam = 0;
      this.airlineCalculations.spam = 0;

      this._AmountService.setAgentCalculations(this.agentCalculations);
      this._AmountService.setAirlineCalculations(this.airlineCalculations);
    }

    return ret;
  }

  private showTOCAOnScreen(): boolean {
    const ret = this.conf.taxOnCommissionEnabled;

    if (!ret) {
      this.agentCalculations.taxOnCommission = 0;
      this.airlineCalculations.taxOnCommission = 0;

      this._AmountService.setAgentCalculations(this.agentCalculations);
      this._AmountService.setAirlineCalculations(this.airlineCalculations);
    }
    return ret;

  }
}
