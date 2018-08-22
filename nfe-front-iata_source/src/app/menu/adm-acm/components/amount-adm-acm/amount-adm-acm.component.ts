import { Component, OnInit, Injector, Input } from '@angular/core';
import { AcdmConfigurationService } from '../../services/adm-acm-configuration.service';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';

@Component({
  selector: 'bspl-amount-adm-acm',
  templateUrl: './amount-adm-acm.component.html',
  styleUrls: ['./amount-adm-acm.component.scss']
})
export class AmountAdmAcmComponent extends ReactiveFormHandler implements OnInit {

  configuration: AdmAcmConfiguration;

  public taxes = [1, 2];
  private taxOnCommissionSign: number;  // sign on TOCA (1/-1)

  constructor(
    private _acdmConfigurationService: AcdmConfigurationService,
  ) {
    super();
    this.subscriptions.push(
      this._acdmConfigurationService
        .getConfiguration()
        .subscribe(data => {
          this.configuration = data;
          this.taxOnCommissionSign = this.configuration.taxOnCommissionSign;
        })
    );
  }

  ngOnInit() {

  }

  // @Input() isADM: boolean;

  // public totalAmount: InputAmount = this.getEmptyInput();

  // /* Data from service */
  // decimals: number;
  // currency: string;
  // typeSimpleView: boolean;
  // total = 0;

  // showSpam: boolean;
  // showTOCA: boolean;

  // private showCPTax: boolean;
  // private showMFTax: boolean;

  // private validTaxes: boolean;          // check if (+)Taxes = Amount[tax]
  // private conf: AdmAcmConfiguration;
  // /* Data from service */

  // /* Data to service */
  // agentCalculations: InputAmountServer = new InputAmountServer();
  // airlineCalculations: InputAmountServer = new InputAmountServer();
  // totalCalculations: InputAmountServer = new InputAmountServer();
  // private taxMiscellaneousFees: TaxAmountServer[] = [];
  // subType: string;
  // private spdrType: string;
  // private checkNR = false;
  // /* Data to service */

  // private TAX_PATTERN = /^(([O][ABC][A-Z0-9./-]{0,6})|([A-Z0-9]{2})|([X][F](([A-Z]{3})([0-9]{1,3})?)?))$/;

  // constructor(
  //   injector: Injector,
  //   private _AdmAcmService: AdmAcmService,
  //   private _AmountService: AmountService
  // ) {
  //   this._AdmAcmService.getConfiguration().subscribe(value => {
  //     if (value != null) {
  //       this.conf = value;
  //       this.taxOnCommissionSign = value.taxOnCommissionSign;

  //       this.showCPTax = this.showCPTaxOnScreen();
  //       this.showMFTax = this.showMFTaxOnScreen();
  //       this.showSpam = this.showSpamOnScreen();
  //       this.showTOCA = this.showTOCAOnScreen();
  //     }
  //   });

  //   this._AdmAcmService.getSpan().subscribe(value => {
  //     this.checkNR = value;
  //     this.showSpam = this.showSpamOnScreen();
  //   });

  //   this._AdmAcmService.getSpdr().subscribe(value => {
  //     this.spdrType = value;
  //     this.showCPTax = this.showCPTaxOnScreen();
  //     this.showMFTax = this.showMFTaxOnScreen();
  //   });

  //   this._AmountService.getTotal().subscribe(value => this.total = value);

  //   this._AmountService.getValidTaxes().subscribe(value => this.validTaxes = value);

  //   // this._AmountService.getAgentCalculations().subscribe(agentCalc => {
  //   //   this.agentCalculations.commission       = agentCalc.commission;
  //   //   this.agentCalculations.fare             = agentCalc.fare;
  //   //   this.agentCalculations.spam             = agentCalc.spam;
  //   //   this.agentCalculations.tax              = agentCalc.tax;
  //   //   this.agentCalculations.taxOnCommission  = agentCalc.taxOnCommission;
  //   // });

  //   // this._AmountService.getAirlineCalculations().subscribe(airlineCalc => {
  //   //   this.airlineCalculations.commission       = airlineCalc.commission;
  //   //   this.airlineCalculations.fare             = airlineCalc.fare;
  //   //   this.airlineCalculations.spam             = airlineCalc.spam;
  //   //   this.airlineCalculations.tax              = airlineCalc.tax;
  //   //   this.airlineCalculations.taxOnCommission  = airlineCalc.taxOnCommission;
  //   // });

  //   this._AmountService.getTaxMiscellaneousFees().subscribe(taxMiscellaneous => {
  //     this.taxMiscellaneousFees = taxMiscellaneous;
  //   });

  //   this._AdmAcmService.getCurrency().subscribe(data => {
  //     this.decimals = data ? data.decimals : 0;
  //     this.currency = data ? data.code : '';
  //     this.resetData();
  //   });

  //   this._AdmAcmService.getSubtype().subscribe(value => {
  //     const list = ['SPDR', 'SPCR', 'ACMA', 'ADMA'];
  //     this.subType = value;
  //     this.typeSimpleView = list.indexOf(value) >= 0;
  //     this.resetData();
  //   });
  // }

  // ngOnInit() {
  //   this.taxes = [this.getEmptyInput()];
  // }

  // resetData() {
  //   this.agentCalculations = new InputAmountServer();
  //   this.airlineCalculations = new InputAmountServer();
  //   this.clean();

  //   this.setDecimals(this.agentCalculations);
  //   this.setDecimals(this.airlineCalculations);
  //   this.calculateTotalOnAmount();

  //   this._AmountService.setAgentCalculations(this.agentCalculations);
  //   this._AmountService.setAirlineCalculations(this.airlineCalculations);
  // }

  // clean() {
  //   this.taxes = [this.getEmptyInput()];
  //   this._AmountService.setTaxMiscellaneousFees([]);
  // }

  // private setDecimals(elem: InputAmountServer) {
  //   elem.commission =       Number(Number(elem.commission).toFixed(this.decimals));
  //   elem.fare =             Number(Number(elem.fare).toFixed(this.decimals));
  //   elem.spam =             Number(Number(elem.spam).toFixed(this.decimals));
  //   elem.tax =              Number(Number(elem.tax).toFixed(this.decimals));
  //   elem.taxOnCommission =  Number(Number(elem.taxOnCommission).toFixed(this.decimals));
  // }

  // checkSimpleView() {
  //   return !this.typeSimpleView;
  // }

  // addTax() {
  //   const index = this.taxes.findIndex((v) => v.name.trim() == '');
  //   if (index < 0) {
  //     this.taxes.push(this.getEmptyInput());
  //   }
  // }

  // validateName(pos: number): void {
  //   this.taxes[pos].name = this.taxes[pos].name.toUpperCase();
  //   this.taxes[pos].name = this.TAX_PATTERN.test(this.taxes[pos].name) ? this.taxes[pos].name : '';

  //   if (!this.showCPTax && this.taxes[pos].name == 'CP') {
  //     this.taxes[pos].name = '';
  //   }

  //   if (!this.showMFTax && this.taxes[pos].name == 'MF') {
  //     this.taxes[pos].name = '';
  //   }

  //   this.calculateOnTax();
  // }

  // populate() {
  //   console.log('on populate');
  // }

  // simpleCalculateFare() {
  //   const total = this.total;

  //   if (this.isADM) {
  //     this.airlineCalculations.fare = total;
  //     this.setDecimals(this.airlineCalculations);
  //     this._AmountService.setAirlineCalculations(this.airlineCalculations);
  //   } else {
  //     this.agentCalculations.fare = total;
  //     this.setDecimals(this.agentCalculations);
  //     this._AmountService.setAgentCalculations(this.agentCalculations);
  //   }

  //   this._AmountService.setTotal(total);
  // }

  // calculateTotalOnAmount() {
  //   const calcType = this.checkFailSide();
  //   this.setDecimals(this.agentCalculations);
  //   this.setDecimals(this.airlineCalculations);

  //   this.totalCalculations.fare =
  //     (calcType * this.agentCalculations.fare) +
  //     (-1 * calcType * this.airlineCalculations.fare);

  //   this.totalCalculations.tax =
  //     (calcType * this.agentCalculations.tax) +
  //     (-1 * calcType * this.airlineCalculations.tax);

  //   this.totalCalculations.commission =
  //     (calcType * this.agentCalculations.commission) +
  //     (-1 * calcType * this.airlineCalculations.commission);

  //   this.totalCalculations.spam =
  //     (calcType * this.agentCalculations.spam) +
  //     (-1 * calcType * this.airlineCalculations.spam);

  //   this.totalCalculations.taxOnCommission =
  //     (calcType * this.agentCalculations.taxOnCommission) +
  //     (-1 * calcType * this.airlineCalculations.taxOnCommission);

  //   this.totalAmount.agentValue =   this.calculateTotal(this.agentCalculations);
  //   this.totalAmount.airlineValue = this.calculateTotal(this.airlineCalculations);
  //   this.totalAmount.dif =          this.calculateTotal(this.totalCalculations);

  //   this._AmountService.setAgentCalculations(this.agentCalculations);
  //   this._AmountService.setAirlineCalculations(this.airlineCalculations);
  //   this._AmountService.setTotal(this.totalAmount.dif);
  // }

  // checkFailSide() {
  //   let aux;
  //   switch (this.spdrType) {
  //     case 'I':
  //     case 'X':
  //     case 'E':
  //       aux = this.isADM ? -1 : 1;
  //     break;
  //     case 'R':
  //       aux = this.isADM ? 1 : -1;
  //       break;
  //   }
  //   return aux;
  // }

  // calculateOnTax() {
  //   this.taxMiscellaneousFees = [];

  //   for (let x of this.taxes) {
  //     if (x.name.trim() != '') {
  //       this.setDifOnRow(x);
  //       const agent = +x.agentValue;
  //       const airline = +x.airlineValue;
  //       this.taxMiscellaneousFees.push(
  //         {type: x.name,
  //           agentAmount: +agent.toFixed(this.decimals),
  //           airlineAmount: +airline.toFixed(this.decimals)
  //         });
  //     }
  //   }

  //   this._AmountService.setTaxMiscellaneousFees(this.taxMiscellaneousFees);
  // }

  // removeTax(num: number) {
  //   if (this.taxes.length > 1) {
  //     this.taxes.splice(num, 1);
  //   } else {
  //     this.taxes = [this.getEmptyInput()];
  //   }

  //   for (let x of this.taxes) {
  //     this.setDifOnRow(x);
  //     const agent = +x.agentValue;
  //     const airline = +x.airlineValue;
  //     this.taxMiscellaneousFees.push(
  //       {type: x.name,
  //         agentAmount: +agent.toFixed(this.decimals),
  //         airlineAmount: +airline.toFixed(this.decimals)
  //       });
  //   }

  //   this._AmountService.setTaxMiscellaneousFees(this.taxMiscellaneousFees);
  // }

  // private calculateTotal(elem: InputAmountServer): number {
  //   return Number(elem.fare)
  //        + Number(elem.tax)
  //        - Number(elem.commission)
  //        - Number(elem.spam)
  //        + (this.conf.taxOnCommissionSign * Number(elem.taxOnCommission));
  // }

  // private setDifOnRow(row) {
  //   row.dif =
  //     Number((Number(row.airlineValue) * (this.isADM ? 1 : -1))
  //     + (Number(row.agentValue) * (this.isADM ? -1 : 1))).toFixed(this.decimals);
  // }

  // private getEmptyInput(): InputAmount {
  //   return { name: '', agentValue: 0, airlineValue: 0, dif: 0 };
  // }

  // private showCPTaxOnScreen(): boolean {
  //   const ret = (this.conf.cpPermittedForConcerningIssue && this.spdrType == 'I') ||
  //                 (this.conf.cpPermittedForConcerningRefund && this.spdrType == 'R');

  //   if (!ret) {
  //     const aux: number = this.taxes.findIndex((x) => x.name.toUpperCase() == 'CP');
  //     if (aux >= 0) {
  //       this.removeTax(aux);
  //     }
  //   }

  //   return ret;
  // }

  // private showMFTaxOnScreen(): boolean {
  //   const ret = (this.conf.mfPermittedForConcerningIssue && this.spdrType == 'I') ||
  //                 (this.conf.mfPermittedForConcerningRefund && this.spdrType == 'R');

  //   if (!ret) {
  //     const aux: number = this.taxes.findIndex((x) => x.name.toUpperCase() == 'MF');
  //     if (aux >= 0) {
  //       this.removeTax(aux);
  //     }
  //   }

  //   return ret;
  // }

  // private showSpamOnScreen(): boolean {
  //   const ret = this.conf.nridAndSpamEnabled && this.checkNR;

  //   if (!ret) {
  //     this.agentCalculations.spam = 0;
  //     this.airlineCalculations.spam = 0;

  //     this.setDecimals(this.agentCalculations);
  //     this.setDecimals(this.airlineCalculations);
  //     this._AmountService.setAgentCalculations(this.agentCalculations);
  //     this._AmountService.setAirlineCalculations(this.airlineCalculations);
  //   }

  //   return ret;
  // }

  // private showTOCAOnScreen(): boolean {
  //   const ret = this.conf.taxOnCommissionEnabled;

  //   if (!ret) {
  //     this.agentCalculations.taxOnCommission = 0;
  //     this.airlineCalculations.taxOnCommission = 0;

  //     this.setDecimals(this.agentCalculations);
  //     this.setDecimals(this.airlineCalculations);
  //     this._AmountService.setAgentCalculations(this.agentCalculations);
  //     this._AmountService.setAirlineCalculations(this.airlineCalculations);
  //   }

  //   return ret;
  // }
}